using CounterStrikeSharp.API;
using CounterStrikeSharp.API.Core;
using CounterStrikeSharp.API.Core.Attributes.Registration;
using CounterStrikeSharp.API.Modules.Commands;
using CounterStrikeSharp.API.Modules.Commands.Targeting;
using CounterStrikeSharp.API.Modules.Utils;
using Microsoft.Extensions.Logging;

namespace IxigoPlugin;

public class IxigoPlugin : BasePlugin
{
    public override string ModuleName => "Ixigo Plugin";

    public override string ModuleVersion => "0.0.1";

    public override void Load(bool hotReload)
    {
        Console.WriteLine("Ixigo plugin loaded!");
    }

    [GameEventHandler]
    public HookResult OnWinPanleMatch(EventCsWinPanelMatch @event, GameEventInfo info)
    {
        Logger.LogInformation("Event triggered: cs_win_panel_match");
        WriteEventToFile("End Map");
        return HookResult.Continue;
    }

    [GameEventHandler]
    public HookResult OnRoundStart(EventRoundStart @event, GameEventInfo info)
    {
        Logger.LogInformation("Event triggered: round_start");
        WriteEventToFile("Round Started");
        return HookResult.Continue;
    }

    [GameEventHandler]
    public HookResult OnRoundEnd(EventRoundEnd @event, GameEventInfo info)
    {
        Logger.LogInformation("Event triggered: round_end");
        WriteEventToFile("Round End");
        return HookResult.Continue;
    }

    [GameEventHandler]
    public HookResult OnRoundAnnounceWarmup(EventRoundAnnounceWarmup @event, GameEventInfo info)
    {
        Logger.LogInformation("Event triggered: round_announce_warmup");
        WriteEventToFile("round_announce_warmup");
        return HookResult.Continue;
    }

    [ConsoleCommand("ixigo_move", "Move players.")]
    [CommandHelper(1, "<comma separate list of steam IDs>")]
    public void OnSwapCommand(CCSPlayerController? caller, CommandInfo info)
    {

        var players = new Target("@all").GetTarget(info.CallingPlayer)?.Players;
        var stringList = info.GetArg(1).Split(',').ToList();

        players?.ForEach(p =>
        {
            if (p.IsBot)
            {
                return;
            }

            if (stringList.Contains(p.SteamID.ToString()))
            {
                p.ChangeTeam(CsTeam.Terrorist);
                Server.PrintToChatAll(FormatAdminMessage($"{ChatColors.Blue}[Plugin] Player {p.PlayerName} moved to T"));
            }
            else
            {
                p.ChangeTeam(CsTeam.CounterTerrorist);
                Server.PrintToChatAll(FormatAdminMessage($"{ChatColors.Blue}[Plugin] Player {p.PlayerName} moved to CT"));
            }
        });
    }


    internal static string FormatMessage(string message) => $" {ChatColors.Lime}[BasicAdmin]{ChatColors.Default} {message}";
    private string FormatAdminMessage(string message) => $" [Admin] {message}";

    private void WriteEventToFile(string eventName)
    {
        // /home/marco/cs2/game/bin/linuxsteamrt64/event.txt
        File.WriteAllText("event.txt", eventName);
    }
}