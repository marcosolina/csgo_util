using DemoInfo;
using DemParser.Constants;
using System;
using Newtonsoft.Json;
using Newtonsoft.Json.Linq;

namespace DemParser.Core.Models.Serialisation
{
	public class EndReasonToStringConverter : JsonConverter
	{
		public override void WriteJson(JsonWriter writer, object value, JsonSerializer serializer)
		{
			string reasonAsString;
			RoundEndReason reason = (RoundEndReason)value;
			switch (reason)
			{
				case RoundEndReason.CTWin:
					reasonAsString = AppConstants.CT_WIN;
					break;
				case RoundEndReason.TerroristWin:
					reasonAsString = AppConstants.T_WIN;
					break;
				case RoundEndReason.TargetBombed:
					reasonAsString = AppConstants.BOMB_EXPLODED;
					break;
				case RoundEndReason.BombDefused:
					reasonAsString = AppConstants.BOMB_DEFUSED;
					break;
				case RoundEndReason.CTSurrender:
					reasonAsString = AppConstants.CT_SURRENDER;
					break;
				case RoundEndReason.TerroristsSurrender:
					reasonAsString = AppConstants.T_SURRENDER;
					break;
				case RoundEndReason.TargetSaved:
					reasonAsString = AppConstants.TARGET_SAVED;
					break;
				default:
					reasonAsString = AppConstants.UNKNOWN;
					break;
			}
			serializer.Serialize(writer, reasonAsString);
		}

		public override object ReadJson(JsonReader reader, Type objectType, object existingValue, JsonSerializer serializer)
		{
			JToken jt = JToken.ReadFrom(reader);
			switch ((string)jt)
			{
				case AppConstants.CT_WIN:
					return RoundEndReason.CTWin;
				case AppConstants.T_WIN:
					return RoundEndReason.TerroristWin;
				case AppConstants.BOMB_EXPLODED:
					return RoundEndReason.TargetBombed;
				case AppConstants.BOMB_DEFUSED:
					return RoundEndReason.BombDefused;
				case AppConstants.CT_SURRENDER:
					return RoundEndReason.CTSurrender;
				case AppConstants.T_SURRENDER:
					return RoundEndReason.TerroristsSurrender;
				case AppConstants.TARGET_SAVED:
					return RoundEndReason.TargetSaved;
				default:
					return RoundEndReason.Draw;
			}
		}

		public override bool CanConvert(Type objectType)
		{
			return typeof(RoundEndReason) == objectType;
		}
	}
}
