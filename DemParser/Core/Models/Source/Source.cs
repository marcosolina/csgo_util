using System.Collections.Generic;
using System.Drawing;

namespace Core.Models.Source
{
    public abstract class Source
    {
        public string Name { get; set; }

        public string Label { get; set; }


        public static Source Factory(string name)
        {
            switch (name)
            {
                case Valve.NAME:
                    return new Valve();
                case Esea.NAME:
                    return new Esea();
                case Ebot.NAME:
                    return new Ebot();
                case Faceit.NAME:
                    return new Faceit();
                case Cevo.NAME:
                    return new Cevo();
                case PopFlash.NAME:
                    return new PopFlash();
                case Esl.NAME:
                    return new Esl();
                case Wanmei.NAME:
                    return new Wanmei();
                case Esportal.NAME:
                    return new Esportal();
                default:
                    return null;
            }
        }

        public static List<Source> Sources = new List<Source>
        {
            new Cevo(),
            new Ebot(),
            new Esea(),
            new Esl(),
            new Faceit(),
            new PopFlash(),
            new Valve(),
            new Wanmei(),
            new Esportal(),
        };
    }
}
