using EasyNetQ;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace Agenda_WebApplication.Contatos
{
    public static class RabbitBusFactory
    {
        //todo
        private const string CONNECTION_STRING = "XXXXXX";

        public static IBus CriarRabbitBus()
        {
            return RabbitHutch.CreateBus(CONNECTION_STRING, serviceRegister => 
                serviceRegister.Register<ISerializer>(serviceProvider => new ContatosJsonSerializer(new ContatoTypeNameSerializer()))
                .Register<ITypeNameSerializer>(s => new ContatoTypeNameSerializer()));
        }
    }
}