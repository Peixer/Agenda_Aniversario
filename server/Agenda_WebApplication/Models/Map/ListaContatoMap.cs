using FluentNHibernate.Data;
using FluentNHibernate.Mapping;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace Agenda_WebApplication.Models.Map
{
    public class ListaContatosMap : ClassMap<ListaContatos>
    {
        private const string NOME_TABELA = "listaContatos";

        public ListaContatosMap()
        {
            Table(NOME_TABELA);
            Id(x => x.Id).Unique().GeneratedBy.Identity();
            Map(x => x.Nome).Length(100).Not.Nullable();
            Map(x => x.Senha).Length(100).Nullable();

            HasMany(x => x.Contatos).Cascade.All();
        }
    }
}