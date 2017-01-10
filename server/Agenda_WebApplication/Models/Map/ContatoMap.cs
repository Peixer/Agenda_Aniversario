using FluentNHibernate.Mapping;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace Agenda_WebApplication.Models.Map
{
    public class ContatoMap : ClassMap<Contato>
    {
        private const string NOME_TABELA = "contato";
        public ContatoMap()
        {
            Table(NOME_TABELA);
            Id(x => x.Id).Unique().GeneratedBy.Identity();
            Map(x => x.nome, "NOME").Length(60).Not.Nullable();
            Map(x => x.email, "EMAIL").Length(50).Not.Nullable();
            Map(x => x.date, "DATA_ANIVERSARIO").Not.Nullable();
            Map(x => x.bytesDaFoto,"BYTES_FOTO").Nullable();

            References(x => x.ListaContatos).Cascade.None();
        }
    }
}