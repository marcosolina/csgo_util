using Newtonsoft.Json;

namespace DemParser.Core.Models
{
	public class HeatmapPoint : MapPoint
	{
		[JsonIgnore]
		public byte Intensity { get; set; } = 100;
	}
}
