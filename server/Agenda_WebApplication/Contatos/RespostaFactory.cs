using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace Agenda_WebApplication.Contatos
{
    public static class RespostaFactory
    {
        public static Resposta CriarRepostaDeErroInesperado()
        {
            return new Resposta() { mensagem = "Aconteceu um erro inesperado", status = "ERRO" };
        }
    }
}