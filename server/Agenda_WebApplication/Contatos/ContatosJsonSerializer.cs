using EasyNetQ;
using Newtonsoft.Json;
using System.Text;

namespace Agenda_WebApplication.Contatos
{
    public class ContatosJsonSerializer : ISerializer
    {
        private readonly ITypeNameSerializer typeNameSerializer;
        private readonly JsonSerializerSettings serializerSettings = new JsonSerializerSettings
        {
            TypeNameHandling = TypeNameHandling.Auto
        };

        public ContatosJsonSerializer(ITypeNameSerializer typeNameSerializer)
        {
            this.typeNameSerializer = typeNameSerializer;
        }

        public byte[] MessageToBytes<T>(T message) where T : class
        {
            string mensagem = JsonConvert.SerializeObject(message, serializerSettings);
            return Encoding.UTF8.GetBytes(mensagem);
        }

        public T BytesToMessage<T>(byte[] bytes)
        {
            return JsonConvert.DeserializeObject<T>(Encoding.UTF8.GetString(bytes), serializerSettings);
        }

        public object BytesToMessage(string typeName, byte[] bytes)
        {
            var type = typeNameSerializer.DeSerialize(typeName);
            return JsonConvert.DeserializeObject(Encoding.UTF8.GetString(bytes), type, serializerSettings);
        }
    }
}