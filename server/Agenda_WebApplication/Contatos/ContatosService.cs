using Agenda_WebApplication.Models;
using FluentNHibernate.Cfg;
using FluentNHibernate.Cfg.Db;
using NHibernate;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading;
using System.Threading.Tasks;

namespace Agenda_WebApplication.Contatos
{
    public class ContatosService
    {
        public Resposta Exportar(InformacoesExportacao informacoesExportacao)
        {
            try
            {
                var sessionFactory = CreateSessionFactory();

                using (var session = sessionFactory.OpenSession())
                {
                    ListaContatos lista = session.QueryOver<ListaContatos>().Where(x => x.Nome == informacoesExportacao.nome).And(x => x.Senha == informacoesExportacao.senha).List<ListaContatos>().FirstOrDefault();

                    if (lista == null)
                    {
                        return new Resposta() { mensagem = "Não existe lista de contatos com essas informações", status = "ERRO" };
                    }
                    else
                    {
                        var quantidadeContatos = lista.Contatos.Count;
                        var queueDosContatos = informacoesExportacao.queue;

                        EnviarContatosViaRabbit(lista.Contatos, informacoesExportacao.queue);

                        return new Resposta() { mensagem = "Informações serão enviadas", status = "SUCESSO", quantidade = quantidadeContatos, queue = queueDosContatos };
                    }
                }
            }
            catch (Exception ex)
            {
                Console.WriteLine(ex);
                //DEVE LANÇAR EXCEÇÃO PARA O RAYGUN

                return RespostaFactory.CriarRepostaDeErroInesperado();
            }
        }

        private Task EnviarContatosViaRabbit(IList<Contato> contatos, string queue)
        {
            return Task.Factory.StartNew(() =>
            {
                using (var bus = RabbitBusFactory.CriarRabbitBus())
                {
                    foreach (Contato contato in contatos)
                    {
                        bus.Send<Contato>(queue, contato);
                    }
                }
            });
        }

        public Resposta Importar(InformacoesImportacao informacaoImportacao)
        {
            var nome = informacaoImportacao.nome;
            var senha = informacaoImportacao.senha;
            var queue = informacaoImportacao.queue;
            var quantidade = informacaoImportacao.quantidade;

            ListaContatos lista = new ListaContatos()
            {
                Nome = nome,
                Senha = senha,
                Contatos = new List<Contato>()
            };

            ObterTodosContatosViaRabbit(queue, quantidade, lista);

            try
            {
                SalvarListaDeContatos(lista);

                return new Resposta() { mensagem = "Informações importadas com sucesso", status = "SUCESSO", quantidade = quantidade, queue = queue };
            }
            catch (Exception ex)
            {
                //DEVE LANÇAR EXCEÇÃO PARA O RAYGUN
                return RespostaFactory.CriarRepostaDeErroInesperado();
            }
        }

        private static void ObterTodosContatosViaRabbit(string queue, int quantidade, ListaContatos lista)
        {
            using (var bus = RabbitBusFactory.CriarRabbitBus())
            {
                bus.Receive<Contato>(queue, (contato) =>
                {
                    if (!lista.Contatos.Any(x => x.UUID == contato.UUID))
                    {
                        contato.ListaContatos = lista;
                        lista.Contatos.Add(contato);
                    }
                });

                while (lista.Contatos.Count != quantidade)
                    Thread.Sleep(1000);
            }
        }

        private static void SalvarListaDeContatos(ListaContatos lista)
        {
            var sessionFactory = CreateSessionFactory();

            using (var session = sessionFactory.OpenSession())
            {
                using (var transaction = session.BeginTransaction())
                {
                    session.SaveOrUpdate(lista);

                    transaction.Commit();
                }
            }
        }

        private static ISessionFactory CreateSessionFactory()
        {
            //todo
            string connectionString = "XXXXX";
            return Fluently.Configure()
                .Database(MySQLConfiguration.Standard.ConnectionString(connectionString).ShowSql())
                .Mappings(m => m.FluentMappings.AddFromAssemblyOf<Contato>())
                .BuildSessionFactory();
        }
    }
}