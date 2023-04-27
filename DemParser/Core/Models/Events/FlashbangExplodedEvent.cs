using System.Collections.Generic;
using DemParser.Core.Models.Serialisation;
using Newtonsoft.Json;

namespace DemParser.Core.Models.Events
{
	public class FlashbangExplodedEvent : NadeBaseEvent
	{
		[JsonProperty("flashed_players_steamid")]
		[JsonConverter(typeof(LongListToStringListConverter))]
		public List<long> FlashedPlayerSteamIdList { get; set; } = new List<long>();

		[JsonIgnore]
		public override string Message => "Flashbang exploded";

		public FlashbangExplodedEvent(int tick, float seconds) : base(tick, seconds) { }
	}
}
