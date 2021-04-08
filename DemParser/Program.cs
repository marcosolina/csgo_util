

using DemoInfo;
using DemParser.Analyzer;
using DemParser.Core.Models;
using DemParser.Core.Models.Source;
using System;
using System.IO;

namespace DemParser
{
    class Program
    {
		static void Main(string[] args)
		{
			string demFile = "C:\\demos\\auto0-20210322-212919-820678393-workshop_570181108_de_inferno_winter-IXI-GO__Monday_Nights__Marco_.dem";

			//using (FileStream input = File.OpenRead(args[0])) {
			/*using (FileStream input = File.OpenRead(demFile))
			{
				DemoParser parser = new DemoParser(input);

				parser.ParseHeader();

				parser.WinPanelMatch += (sender, e) =>
				{
					foreach (DemoInfo.Player p in parser.PlayingParticipants)
					{
						if (p.SteamID == 0)
						{
							continue;
						}
						Console.WriteLine(string.Format("{0},{1},{2}", p.AdditionaInformations.Score, p.Name, p.SteamID));
					}
				};

				parser.ParseToEnd();
			}*/

			/*
			 * New Experiment
			 */

			Demo demo = new Demo();
			demo.Path = demFile;
			demo.Source = Source.Factory("valve");
			DemoAnalyzer va = DemoAnalyzer.Factory(demo);
			va.Parser.ParseHeader();
			va.Parser.ParseToEnd();
            va.ProcessAnalyzeEnded();

			Console.WriteLine("Name,SteamID,RWS,Kills,Assists,Deaths,K/D,HS,HS%,FF,EK,BP,BD,MVP,Score,HLTV,5K,4K,3K,2K,1K,TK,TD,KPR,APR,DPR,ADR,TDH,TDA,1v1,1v2,1v3,1v4,1v5,Grenades,Flashes,Fire");

			foreach (Core.Models.Player player in demo.Players)
			{
				string s = player.EseaRws.ToString();
				Console.WriteLine(string.Format("{0},{1},{2},{3},{4},{5},{6},{7},{8},{9},{10},{11},{12},{13},{14},{15},{16},{17},{18},{19},{20},{21},{22},{23},{24},{25},{26},{27},{28},{29},{30},{31},{32},{33},{34},{35},{36}",
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
					player.MolotovThrownCount + player.IncendiaryThrownCount
					));
			}
		}
    }
}
