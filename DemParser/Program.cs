

using DemoInfo;
using System;
using System.IO;

namespace DemParser
{
    class Program
    {
		static void Main(string[] args)
		{
			using (FileStream input = File.OpenRead(args[0])) {

				DemoParser parser = new DemoParser(input);

				parser.ParseHeader();

				parser.WinPanelMatch += (sender, e) =>
				{
					foreach (Player p in parser.PlayingParticipants)
					{
						if(p.SteamID == 0)
                        {
							continue;
                        }
						Console.WriteLine(string.Format("{0},{1},{2}", p.AdditionaInformations.Score, p.Name, p.SteamID));
					}
				};

				parser.ParseToEnd();
			}
			
		}
    }
}
