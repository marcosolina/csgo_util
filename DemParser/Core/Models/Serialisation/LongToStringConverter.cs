using Newtonsoft.Json;
using Newtonsoft.Json.Linq;
using System;

namespace DemParser.Core.Models.Serialisation
{
	public class LongToStringConverter : JsonConverter
	{
		public override void WriteJson(JsonWriter writer, object value, JsonSerializer serializer)
		{
			serializer.Serialize(writer, value.ToString());
		}

		public override object ReadJson(JsonReader reader, Type objectType, object existingValue, JsonSerializer serializer)
		{
			JToken jt = JToken.ReadFrom(reader);
			return jt.Value<long>();
		}

		public override bool CanConvert(Type objectType)
		{
			return typeof(long) == objectType;
		}
	}
}
