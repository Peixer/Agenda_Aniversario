using FluentNHibernate.Data;
using Newtonsoft.Json;
using Newtonsoft.Json.Converters;
using System;
using System.Runtime.Serialization;

namespace Agenda_WebApplication.Models
{
    public class Contato : Entity
    {
        public virtual string nome { get; set; }
        public virtual string UUID { get { return Guid.NewGuid().ToString(); } set { } }
        public virtual string email { get; set; }
        public virtual sbyte[] bytesDaFoto { get; set; }

        [JsonConverter(typeof(CustomDateTimeConverter))]
        public virtual DateTime date { get; set; }

        [JsonIgnore]
        [IgnoreDataMember]
        public virtual ListaContatos ListaContatos { get; set; }
    }

    public class CustomDateTimeConverter : IsoDateTimeConverter
    {
        public CustomDateTimeConverter()
        {
            base.DateTimeFormat = "dd/MM/yyyy";
        }
    }
}