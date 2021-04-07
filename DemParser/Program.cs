

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
			string demFile = "C:\\demos\\auto0-20210405-190514-472545077-de_dust2-IXI-GO__Monday_Nights__Marco_.dem";

			//using (FileStream input = File.OpenRead(args[0])) {
			using (FileStream input = File.OpenRead(demFile))
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
			}

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

			foreach (Core.Models.Player player in demo.Players)
			{
				string s = player.EseaRws.ToString();
				Console.WriteLine(string.Format("{0},{1},{2},{3}", player.EseaRws.ToString(), player.RatingHltv.ToString(), player.Name, player.SteamId));
			}
		}
    }
}
