using Agenda_WebApplication.Models;
using EasyNetQ;
using System;

namespace Agenda_WebApplication.Contatos
{
    public class ContatoTypeNameSerializer : ITypeNameSerializer
    {
        public Type DeSerialize(string typeName)
        {
            return typeof(Contato);
        }

        public string Serialize(Type type)
        {
            return typeof(Contato).ToString();
        }
    }
}