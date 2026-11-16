import json

def generate_json_schema(data):
    if isinstance(data, dict):
        schema = {"type": "object", "properties": {}}
        for key, value in data.items():
            schema["properties"][key] = generate_json_schema(value)
        return schema
    elif isinstance(data, list):
        item_types = set(type(item) for item in data)
        if len(item_types) == 1:
            item_type = next(iter(item_types))
            if item_type in [int, float, str, bool]:
                return {"type": "array", "items": {"type": json_type_mapping[item_type]}}
            else:
                return {"type": "array", "items": generate_json_schema(data[0])}
        else:
            return {"type": "array", "items": {"type": "object"}}
    elif isinstance(data, str):
        return {"type": "string"}
    elif isinstance(data, int):
        return {"type": "integer"}
    elif isinstance(data, float):
        return {"type": "number"}
    elif isinstance(data, bool):
        return {"type": "boolean"}
    elif data is None:
        return {"type": "null"}
    else:
        return {"type": "object"}

# Helper to map Python types to JSON schema types
json_type_mapping = {
    int: "integer",
    float: "number",
    str: "string",
    bool: "boolean"
}

# Example usage
your_json_data = json.load(open("C:\\Users\\Mark\\Downloads\\file (1).json"))
json_schema = generate_json_schema(your_json_data)

print (json_schema)
