using Agenda_WebApplication.Contatos;
using System;
using System.Threading.Tasks;
using System.Web.Http;
using System.Web.Http.Results;

namespace Agenda_WebApplication.Controllers
{
    public class ContatosController : ApiController
    {
        [HttpGet]
        public string OI()
        {
            return "oi";
        }

        [HttpPost]
        public JsonResult<Resposta> Exportar(InformacoesExportacao informacoesExportacao)
        {
            try
            {
                ContatosService contatosService = new ContatosService();
                var resposta = contatosService.Exportar(informacoesExportacao);

                return Json<Resposta>(resposta);
            }
            catch (Exception)
            {
                //Raygun 
                return Json<Resposta>(RespostaFactory.CriarRepostaDeErroInesperado());
            }
        }

        [HttpPost]
        public Task<JsonResult<Resposta>> Importar(InformacoesImportacao informacaoImportacao)
        {
            Task<JsonResult<Resposta>> task = Task.Factory.StartNew(() =>
            {
                ContatosService contatosService = new ContatosService();
                var resposta = contatosService.Importar(informacaoImportacao);

                return Json<Resposta>(resposta);
            });

            return task;
        }
    }
}
