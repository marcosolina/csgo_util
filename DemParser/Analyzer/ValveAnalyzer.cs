using System;
using System.IO;
using System.Linq;
using System.Text;
using System.Threading;
using System.Threading.Tasks;
using Core.Models;
using Core.Models.Events;
using Core.Models.protobuf;
using DemoInfo;
using DemParser.DemoInfo;
using ProtoBuf;
using Player = Core.Models.Player;

namespace DemParser.Analyzer
{
    public class ValveAnalyzer : DemoAnalyzer
    {
        /// <summary>
        /// Used to detect forced pauses during a round.
        /// When it happens, all players are killed and the game is paused.
        /// We don't want to pause the game when only one player killed himself (it can happened).
        /// We track hw many suicides happened in a row.
        /// </summary>
        private int _suicideCount;

        private bool _isRoundFinal = false;

        /// <summary>
        /// Used to support buggy demos such as the one is this issue https://github.com/akiver/CSGO-Demos-Manager/issues/484
        /// The round end officially event of the 14th round AND the round start event of the 15th round are not triggered.
        /// To handle this scenario, this flag is synced with m_iRoundWinStatus and round start events.
        /// Note: even the CSGO client playback system skips the 15th round when forwarding from the 14th to 15th round.
        /// </summary>
        private bool _isRoundStartOccurred = false;

        public ValveAnalyzer(Demo demo)
        {
            // Reset to have update on UI
            demo.ResetStats();
            Demo = demo;
        }

        protected sealed override void RegisterEvents()
        {
            Parser.MatchStarted += HandleMatchStarted;
            Parser.RoundAnnounceMatchStarted += HandleRoundAnnounceMatchStarted;
            Parser.RoundMVP += HandleRoundMvp;
            Parser.PlayerKilled += HandlePlayerKilled;
            Parser.RoundStart += HandleRoundStart;
            Parser.RoundOfficiallyEnd += HandleRoundOfficiallyEnd;
            Parser.BombPlanted += HandleBombPlanted;
            Parser.BombDefused += HandleBombDefused;
            Parser.BombExploded += HandleBombExploded;
            Parser.TickDone += HandleTickDone;
            Parser.WeaponFired += HandleWeaponFired;
            Parser.RoundEnd += HandleRoundEnd;
            Parser.FlashNadeExploded += HandleFlashNadeExploded;
            Parser.ExplosiveNadeExploded += HandleExplosiveNadeExploded;
            Parser.SmokeNadeStarted += HandleSmokeNadeStarted;
            Parser.FireNadeEnded += HandleFireNadeEnded;
            Parser.BotTakeOver += HandleBotTakeOver;
            Parser.LastRoundHalf += HandleLastRoundHalf;
            Parser.SmokeNadeEnded += HandleSmokeNadeEnded;
            Parser.FireNadeStarted += HandleFireNadeStarted;
            Parser.DecoyNadeStarted += HandleDecoyNadeStarted;
            Parser.DecoyNadeEnded += HandleDecoyNadeEnded;
            Parser.PlayerHurt += HandlePlayerHurted;
            Parser.RankUpdate += HandleServerRankUpdate;
            Parser.PlayerDisconnect += HandlePlayerDisconnect;
            Parser.PlayerTeam += HandlePlayerTeam;
            Parser.SayText2 += HandleSayText2;
            Parser.FreezetimeEnded += HandleFreezetimeEnded;
            Parser.RoundFinal += HandleRoundFinal;
            Parser.WinPanelMatch += HandleWinPanelMatch;
            Parser.GamePhaseChanged += HandleGamePhaseChanged;
            Parser.RoundWinStatusChanged += HandleRoundWinStatusChanged;
            Parser.POVRecordingPlayerDetected += HandlePOVPlayerRecordingDetected;
            Parser.ServerInfo += HandleServerInfo;
        }

        public override async Task<Demo> AnalyzeDemoAsync(CancellationToken token, Action<string, float> progressCallback = null)
        {
            byte[] netMessageDecryptionKey = GetDecryptionKey(Demo.Path);
            Parser = new DemoParser(File.OpenRead(Demo.Path), netMessageDecryptionKey);
            RegisterEvents();
            ProgressCallback = progressCallback;
            Parser.ParseHeader();

            await Task.Run(() => Parser.ParseToEnd(token), token);

            ProcessAnalyzeEnded();

            return Demo;
        }

        private static byte[] GetDecryptionKey(string demoPath)
        {
            string infoFilePath = demoPath + ".info";
            bool isInfoFileExits = File.Exists(infoFilePath);
            if (isInfoFileExits)
            {
                using (FileStream infoFile = File.OpenRead(infoFilePath))
                {
                    try
                    {
                        CDataGCCStrike15v2MatchInfo matchInfo = Serializer.Deserialize<CDataGCCStrike15v2MatchInfo>(infoFile);
                        string publicKeyAsString = $"{matchInfo.Watchablematchinfo.ClDecryptdataKeyPub:X}".PadLeft(16, '0').ToUpper();

                        return Encoding.ASCII.GetBytes(publicKeyAsString);
                    }
                    catch (Exception)
                    {
                        // ignore corrupted .info file
                    }
                }
            }

            return null;
        }

        #region Events Handlers

        private void HandleServerRankUpdate(object sender, RankUpdateEventArgs e)
        {
            Player player = Demo.Players.FirstOrDefault(p => p.SteamId == e.SteamId);
            if (player != null)
            {
                player.RankNumberOld = e.RankOld;
                player.RankNumberNew = e.RankNew;
                player.WinCount = e.WinCount;
            }
        }

        private void HandleBotTakeOver(object sender, BotTakeOverEventArgs e)
        {
            if (!IsMatchStarted || e.Taker == null)
            {
                return;
            }

            Player player = Demo.Players.FirstOrDefault(p => p.SteamId == e.Taker.SteamID);
            if (player != null)
            {
                player.IsControllingBot = true;
            }
        }

        protected void HandleLastRoundHalf(object sender, LastRoundHalfEventArgs e)
        {
            if (!IsMatchStarted)
            {
                return;
            }

            IsLastRoundHalf = true;
        }

        private void HandleRoundFinal(object sender, RoundFinalEventArgs roundFinalEventArgs)
        {
            _isRoundFinal = true;
        }

        protected override void HandleMatchStarted(object sender, MatchStartedEventArgs e)
        {
            if (IsMatchStarted)
            {
                Demo.ResetStats(false);
            }

            StartMatch();
        }

        private void HandleGamePhaseChanged(object sender, GamePhaseChangedArgs e)
        {
            if (!IsMatchStarted)
            {
                return;
            }

            var valueChanged = e.NewGamePhase != e.OldGamePhase;
            if (!valueChanged)
            {
                return;
            }

            switch (e.NewGamePhase)
            {
                case GamePhase.TeamSideSwitch:
                    SwapTeams();
                    break;
                case GamePhase.GameHalfEnded:
                    if (!IsLastRoundHalf)
                    {
                        AddCurrentRound();
                    }
                    IsLastRoundHalf = true;
                    break;
            }
        }

        private void HandleRoundWinStatusChanged(object sender, RoundWinStatusChangedArgs e)
        {
            if (!IsMatchStarted)
            {
                return;
            }

            // m_iRoundWinStatus may be updated several times with the same value at the same tick with POV demos.
            var valueChanged = e.OldWinStatus != e.NewWinStatus;
            bool isRoundStart = e.NewWinStatus == RoundWinStatus.Unassigned && valueChanged;
            if (isRoundStart && !_isRoundStartOccurred)
            {
                AddCurrentRound();
                CreateNewRound();
            }
            else
            {
                _isRoundStartOccurred = false;
            }
        }

        protected void HandleWinPanelMatch(object sender, WinPanelMatchEventArgs e)
        {
            IsMatchStarted = false;
        }

        protected void HandleRoundAnnounceMatchStarted(object sender, RoundAnnounceMatchStartedEventArgs e)
        {
            if (!IsMatchStarted)
            {
                StartMatch();
            }
        }

        protected override void HandleRoundStart(object sender, RoundStartedEventArgs e)
        {
            RegisterUnknownPlayers();
            
            if (!IsMatchStarted)
            {
                return;
            }

            _isRoundStartOccurred = true;
            _suicideCount = 0;
            IsLastRoundHalf = false;

            CreateNewRound();
        }

        protected new void HandleRoundEnd(object sender, RoundEndedEventArgs e)
        {
            base.HandleRoundEnd(sender, e);

            if (!IsMatchStarted || IsFreezetime)
            {
                return;
            }

            if (IsLastRoundHalf)
            {
                AddCurrentRound();
            }

            if (IsOvertime && IsLastRoundHalf)
            {
                IsHalfMatch = false;
            }
        }

        protected new void HandleRoundOfficiallyEnd(object sender, RoundOfficiallyEndedEventArgs e)
        {
            base.HandleRoundOfficiallyEnd(sender, e);

            if (!IsMatchStarted || IsFreezetime)
            {
                return;
            }

            if (_isRoundFinal)
            {
                _isRoundFinal = false;
                if (IsOvertime)
                {
                    Demo.Overtimes.Add(CurrentOvertime);
                    IsHalfMatch = !IsHalfMatch;
                }
                else
                {
                    IsOvertime = true;
                }

                CreateNewOvertime();
            }
        }

        protected new void HandlePlayerKilled(object sender, PlayerKilledEventArgs e)
        {
            if (!IsMatchStarted || IsFreezetime || e.Victim == null)
            {
                return;
            }
            
            Weapon weapon = Weapon.WeaponList.FirstOrDefault(w => w.Element == e.Weapon.Weapon);
            if (weapon == null)
            {
                return;
            }

            Player killed = Demo.Players.FirstOrDefault(player => player.SteamId == e.Victim.SteamID);
            Player killer = null;

            KillEvent killEvent = new KillEvent(Parser.IngameTick, Parser.CurrentTime)
            {
                KillerSteamId = e.Killer?.SteamID ?? 0,
                KillerName = e.Killer?.Name ?? "World",
                KillerSide = e.Killer?.Team.ToSide() ?? Side.None,
                Weapon = weapon,
                KillerVelocityX = e.Killer?.Velocity.X ?? 0,
                KillerVelocityY = e.Killer?.Velocity.Y ?? 0,
                KillerVelocityZ = e.Killer?.Velocity.Z ?? 0,
                KillerIsBlinded = e.Killer?.FlashDuration > 0,
                KilledSteamId = e.Victim.SteamID,
                KilledName = e.Victim.Name,
                KilledSide = e.Victim.Team.ToSide(),
                VictimIsBlinded = e.Victim.FlashDuration > 0,
                RoundNumber = CurrentRound.Number,
                IsKillerCrouching = e.Killer?.IsDucking ?? false,
                IsHeadshot = e.Headshot,
                Point = new KillHeatmapPoint
                {
                    KillerX = e.Killer?.Position.X ?? 0,
                    KillerY = e.Killer?.Position.Y ?? 0,
                    KillerZ = e.Killer?.Position.Z ?? 0,
                    VictimX = e.Victim.Position.X,
                    VictimY = e.Victim.Position.Y,
                    VictimZ = e.Victim.Position.Z,
                },
                TimeDeathSeconds = (float)Math.Round((Parser.IngameTick - CurrentRound.FreezetimeEndTick) / Demo.Tickrate, 2),
            };

            bool killerIsBot = e.Killer != null && e.Killer.SteamID == 0;
            bool victimIsBot = e.Victim.SteamID == 0;
            bool assisterIsBot = e.Assister != null && e.Assister.SteamID == 0;

            if (e.Killer != null)
            {
                killer = Demo.Players.FirstOrDefault(player => player.SteamId == e.Killer.SteamID);
            }

            if (killer != null)
            {
                if (e.Victim.SteamID != killer.SteamId)
                {
                    if (!killer.RoundsMoneyEarned.ContainsKey(CurrentRound.Number))
                    {
                        killer.RoundsMoneyEarned[CurrentRound.Number] = weapon.KillAward;
                    }
                    else
                    {
                        killer.RoundsMoneyEarned[CurrentRound.Number] += weapon.KillAward;
                    }
                }
                else
                {
                    // Player suicide, hack to detect pause forced during a match
                    // Happended during the match SK vs VP on train during Atlanta 2017
                    // All players are killed and the game is paused (freeze time)
                    if (++_suicideCount == 6)
                    {
                        BackupToLastRound();
                    }

                    return;
                }

                killEvent.KillerTeam = killer.TeamName;
                killEvent.KillerIsControllingBot = e.Killer.SteamID != 0 && killer.IsControllingBot;
                ProcessTradeKill(killEvent);
            }

            if (killed != null)
            {
                // suicide, probably because he missed the jump from upper B on train :)
                if (e.Killer == null)
                {
                    killed.SuicideCount++;
                }

                killEvent.KilledIsControllingBot = e.Victim.SteamID != 0 && killed.IsControllingBot;
                killEvent.KilledTeam = killed.TeamName;
                killed.TimeDeathRounds[CurrentRound.Number] = killEvent.TimeDeathSeconds;
            }

            // Human killed human
            if (!killerIsBot && !victimIsBot)
            {
                if (killer != null && killed != null)
                {
                    killed.IsAlive = false;
                    // TK
                    if (e.Killer.Team == e.Victim.Team)
                    {
                        killer.Kills.Add(killEvent);
                        killed.Deaths.Add(killEvent);
                    }
                    else
                    {
                        // Regular kill
                        if (!killer.IsControllingBot)
                        {
                            killer.Kills.Add(killEvent);
                        }

                        if (!killed.IsControllingBot)
                        {
                            killed.Deaths.Add(killEvent);
                        }
                    }
                }
            }

            // Human killed a bot
            if (!killerIsBot && victimIsBot)
            {
                killer?.Kills.Add(killEvent);
            }

            // A bot killed a human
            if (killerIsBot && !victimIsBot)
            {
                // TK or not we add a death to the human
                killed?.Deaths.Add(killEvent);
            }

            // Add assist if there was one
            if (e.Assister != null && !assisterIsBot && e.Assister.Team != e.Victim.Team)
            {
                Player assister = Demo.Players.FirstOrDefault(player => player.SteamId == e.Assister.SteamID);
                if (assister != null)
                {
                    killEvent.AssisterSteamId = e.Assister.SteamID;
                    killEvent.AssisterName = e.Assister.Name;
                    killEvent.AssisterIsControllingBot = e.Assister.SteamID != 0 && assister.IsControllingBot;
                    assister.Assists.Add(killEvent);
                }
            }

            // If the killer isn't a bot we can update individual kill, open and entry kills
            if (e.Killer != null && e.Killer.Team != e.Victim.Team && killer != null && !killer.IsControllingBot)
            {
                if (!KillsThisRound.ContainsKey(e.Killer))
                {
                    KillsThisRound[e.Killer] = 0;
                }

                KillsThisRound[e.Killer]++;

                ProcessOpenAndEntryKills(killEvent);
            }

            ProcessClutches();

            CurrentRound.Kills.Add(killEvent);
            Demo.Kills.Add(killEvent);

            if (AnalyzePlayersPosition)
            {
                PositionPoint positionPoint = new PositionPoint
                {
                    X = e.Victim.Position.X,
                    Y = e.Victim.Position.Y,
                    PlayerSteamId = e.Killer?.SteamID ?? 0,
                    PlayerName = e.Killer?.Name ?? string.Empty,
                    Team = e.Killer?.Team.ToSide() ?? Side.None,
                    Event = killEvent,
                    RoundNumber = CurrentRound.Number,
                };
                Demo.PositionPoints.Add(positionPoint);
            }
        }

        #endregion

        #region Process

        private void StartMatch()
        {
            Demo.Rounds.Clear();
            IsMatchStarted = true;

            if (!string.IsNullOrWhiteSpace(Parser.CTClanName))
            {
                Demo.TeamCT.Name = Parser.CTClanName;
            }

            if (!string.IsNullOrWhiteSpace(Parser.TClanName))
            {
                Demo.TeamT.Name = Parser.TClanName;
            }

            Demo.TeamCT.CurrentSide = Side.CounterTerrorist;
            Demo.TeamT.CurrentSide = Side.Terrorist;

            RegisterUnknownPlayers();

            // First round handled here because round_start is raised before begin_new_match
            CreateNewRound();
        }

        #endregion
    }
}
