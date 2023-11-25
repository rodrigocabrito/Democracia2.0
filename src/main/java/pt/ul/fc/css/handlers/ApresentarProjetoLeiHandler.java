package pt.ul.fc.css.handlers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pt.ul.fc.css.entities.Delegado;
import pt.ul.fc.css.entities.ProjetoLei;
import pt.ul.fc.css.entities.Status;
import pt.ul.fc.css.entities.Tema;
import pt.ul.fc.css.facade.dtos.ProjetoLeiDTO;
import pt.ul.fc.css.facade.exceptions.ApplicationException;
import pt.ul.fc.css.facade.exceptions.InvalidDateException;
import pt.ul.fc.css.repositories.DelegadoRepository;
import pt.ul.fc.css.repositories.ProjetoLeiRepository;
import pt.ul.fc.css.services.ProjetoLeiService;

import java.time.LocalDateTime;

@Component
public class ApresentarProjetoLeiHandler {

    @Autowired
    private ProjetoLeiRepository repositorioProjetoLei;
    @Autowired
    private DelegadoRepository repositorioDelegado;

    public ProjetoLeiDTO apresentarProjetoLei(String titulo, String descricao, byte[] anexoPDF,
                                              LocalDateTime validade, Delegado delegado, Tema tema) throws ApplicationException {

        try {
            ProjetoLei projeto = new ProjetoLei();

            if (!projeto.isValidDate(validade)) {
                throw new InvalidDateException("Data invalida.");

            } else {
                projeto.setTitulo(titulo);
                projeto.setDescricao(descricao);
                projeto.setAnexoPDF(anexoPDF);
                projeto.setDataLimite(validade);
                projeto.setDelegado(delegado);
                projeto.setTema(tema);
                projeto.setStatus(Status.ABERTO);
                projeto.addApoio(delegado);

                repositorioProjetoLei.save(projeto);

                delegado.addApoioProjeto(projeto);

                repositorioDelegado.save(delegado);

                return ProjetoLeiService.dtofy(projeto);
            }
        } catch (Exception e) {
            throw new ApplicationException("Erro ao adicionar Projeto de Lei.", e);
        }
    }
}
