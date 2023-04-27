using Newtonsoft.Json;

namespace DemParser.Core.Models.Events
{
	public class MolotovFireEndedEvent : NadeBaseEvent
	{
		[JsonIgnore]
		public override string Message => "Molotov ended";

		public MolotovFireEndedEvent(int tick, float seconds) : base(tick, seconds) { }
	}
}
