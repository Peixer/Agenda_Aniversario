using FluentNHibernate.Data;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace Agenda_WebApplication.Models
{
    public class ListaContatos : Entity
    {
        public virtual string Nome { get; set; }
        public virtual string Senha { get; set; }
        public virtual IList<Contato> Contatos { get; set; }
    }
}