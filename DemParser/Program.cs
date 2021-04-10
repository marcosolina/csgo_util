

using DemoInfo;
using DemParser.Analyzer;
using DemParser.Core.Models;
using DemParser.Core.Models.Source;
using DemParser.Core.Models.Events;
using System;
using System.IO;

namespace DemParser
{
    class Program
    {
		static void Main(string[] args)
		{
			string demFile = ""; 
			demFile = "C:\\demos\\auto0-20210322-212919-820678393-workshop_570181108_de_inferno_winter-IXI-GO__Monday_Nights__Marco_.dem";
			//demFile = "C:\\tmp\\demfiles\\2021-03-29\\auto0-20210329-193658-1526021228-cs_assault-IXI-GO__Monday_Nights__Marco_.dem";
			//demFile = args[0];

			Demo demo = new Demo();
			demo.Path = demFile;
			demo.Source = Source.Factory(Valve.NAME);
			DemoAnalyzer va = DemoAnalyzer.Factory(demo);
			va.Parser.ParseHeader();
			va.Parser.ParseToEnd();
            va.ProcessAnalyzeEnded();

			Console.WriteLine("Name;SteamID;RWS;Kills;Assists;Deaths;K/D;HS;HS%;FF;EK;BP;BD;MVP;Score;HLTV;5K;4K;3K;2K;1K;TK;TD;KPR;APR;DPR;ADR;TDH;TDA;1v1;1v2;1v3;1v4;1v5;Grenades;Flashes;Smokes;Fire;HEDamage;FireDamage;MatchPlayed");

			foreach (Core.Models.Player player in demo.Players)
			{
				string s = player.EseaRws.ToString();
				//additional damage info
				int heDamage = 0;
				int fireDamage = 0;
				//check the player played what percentage of the match
				double matchPlayedPercent = (double)(player.RoundPlayedCount + 1) / (double)(demo.Rounds.Count);
				foreach (PlayerHurtedEvent hurt in player.PlayersHurted)
                {
					if (hurt.AttackerSteamId == player.SteamId && hurt.Weapon.Name.Equals(Weapon.HE)){
						heDamage += hurt.HealthDamage;
					}
					else if (hurt.AttackerSteamId == player.SteamId && (hurt.Weapon.Name.Equals(Weapon.INCENDIARY) || hurt.Weapon.Name.Equals(Weapon.MOLOTOV)))
					{
						fireDamage += hurt.HealthDamage;
					}
				}
				Console.WriteLine(string.Format("{0};{1};{2};{3};{4};{5};{6};{7};{8};{9};{10};{11};{12};{13};{14};{15};{16};{17};{18};{19};{20};{21};{22};{23};{24};{25};{26};{27};{28};{29};{30};{31};{32};{33};{34};{35};{36};{37};{38};{39};{40}",
					player.Name, player.SteamId, player.EseaRws,
					player.KillCount,
					player.AssistCount,
					player.DeathCount,
					player.KillDeathRatio,
					player.HeadshotCount,
					player.HeadshotPercent,
					player.TeamKillCount,
					player.EntryKillLossCount + player.EntryKillWonCount,
					player.BombPlantedCount,
					player.BombDefusedCount,
					player.RoundMvpCount,
					player.Score,
					player.RatingHltv,
					player.FiveKillCount,
					player.FourKillCount,
					player.ThreeKillCount,
					player.TwoKillCount,
					player.OneKillCount,
					player.TradeKillCount,
					player.TradeDeathCount,
					player.KillPerRound,
					player.AssistPerRound,
					player.DeathPerRound,
					player.AverageHealthDamage,
					player.TotalDamageHealthCount,
					player.TotalDamageArmorCount,
					player.Clutch1V1WonCount,
					player.Clutch1V2WonCount,
					player.Clutch1V3WonCount,
					player.Clutch1V4WonCount,
					player.Clutch1V5Count,
					player.HeGrenadeThrownCount,
					player.FlashbangThrownCount,
					player.SmokeThrownCount,
					player.MolotovThrownCount + player.IncendiaryThrownCount,
					heDamage,
					fireDamage,
					matchPlayedPercent
					));
			}
		}
    }
}
