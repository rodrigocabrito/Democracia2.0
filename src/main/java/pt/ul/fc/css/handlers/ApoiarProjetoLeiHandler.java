package pt.ul.fc.css.handlers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pt.ul.fc.css.entities.Cidadao;
import pt.ul.fc.css.entities.Delegado;
import pt.ul.fc.css.entities.ProjetoLei;
import pt.ul.fc.css.facade.dtos.ProjetoLeiDTO;
import pt.ul.fc.css.facade.exceptions.ApplicationException;
import pt.ul.fc.css.repositories.CidadaoRepository;
import pt.ul.fc.css.repositories.DelegadoRepository;
import pt.ul.fc.css.repositories.ProjetoLeiRepository;
import pt.ul.fc.css.services.ProjetoLeiService;

import java.util.Optional;

@Component
public class ApoiarProjetoLeiHandler {

    @Autowired
    private CidadaoRepository repositorioCidadao;
    @Autowired
    private ProjetoLeiRepository repositorioProjetoLei;
    @Autowired
    private DelegadoRepository repositorioDelegado;

    public Optional<ProjetoLeiDTO> apoiarProjetoLei(Long projetoLeiId, Long cidadaoId) throws ApplicationException {
        try {
            Optional<ProjetoLei> op = repositorioProjetoLei.findById(projetoLeiId);
            Optional<Cidadao> oc = repositorioCidadao.findById(cidadaoId);
            op.ifPresent(projeto -> {
                oc.ifPresent(cidadao -> {

                    if (!cidadao.isApoiante(projeto)) {
                        Delegado delegado = repositorioDelegado.findByCC(cidadao.getNrCC());
                        if (delegado != null) { //if it is a delegate

                            if (!delegado.isApoiante(projeto)) {
                                projeto.addApoio(delegado);
                                delegado.addApoioProjeto(projeto);

                                repositorioDelegado.save(delegado);
                                repositorioProjetoLei.save(projeto);
                            }
                        } else {
                            projeto.addApoio(cidadao);
                            cidadao.addApoioProjeto(projeto);

                            repositorioCidadao.save(cidadao);
                            repositorioProjetoLei.save(projeto);
                        }
                    }
                });
            });
            return op.map(ProjetoLeiService::dtofy);

        } catch (Exception e) {
            throw new ApplicationException("Erro ao apoiar projetoLei com id " + projetoLeiId + ".", e);
        }
    }
}
