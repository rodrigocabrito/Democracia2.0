package pt.ul.fc.css.handlers;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pt.ul.fc.css.entities.ProjetoLei;
import pt.ul.fc.css.entities.Status;
import pt.ul.fc.css.facade.exceptions.ApplicationException;
import pt.ul.fc.css.repositories.ProjetoLeiRepository;

import java.util.List;

@Component
public class FecharProjetosLeiExpiradosHandler {

    @Autowired
    private ProjetoLeiRepository repositorioProjetoLei;

    /**
     * Closes all bills with an expired date.
     * @throws ApplicationException if an error occurs closing all the bills.
     */
    @Transactional
    public void fecharProjetosLeiExpirados() throws ApplicationException {
        try {

            List<ProjetoLei> projetos = repositorioProjetoLei.findAll();
            for (ProjetoLei projeto : projetos) {

                if (projeto.hasExpired(projeto.getDataLimite())) {
                    projeto.setStatus(Status.FECHADO);
                    repositorioProjetoLei.save(projeto);
                }
            }
        } catch (Exception e) {
            throw new ApplicationException("Erro ao fechar os Projetos de lei expirados.", e);
        }
    }
}
