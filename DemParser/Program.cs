

using DemoInfo;
using DemParser.Analyzer;
using DemParser.Core.Models;
using System;
using System.IO;

namespace DemParser
{
    class Program
    {
		static void Main(string[] args)
		{
			string demFile = "C:\\tmp\\demfiles\\2021-03-29\\auto0-20210329-193658-1526021228-cs_assault-IXI-GO__Monday_Nights__Marco_.dem";

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
			EseaAnalyzer ea = new EseaAnalyzer(demo);

			ea.Parser.ParseHeader();
			ea.Parser.ParseToEnd();
            ea.ProcessAnalyzeEnded();

			foreach (Core.Models.Player player in demo.Players)
			{
				string s = player.EseaRws.ToString();
				Console.WriteLine(s);
			}
		}
    }
}
