package pt.ul.fc.css;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import pt.ul.fc.css.facade.dtos.CidadaoDTO;
import pt.ul.fc.css.facade.dtos.DelegadoDTO;
import pt.ul.fc.css.facade.dtos.ProjetoLeiDTO;
import pt.ul.fc.css.facade.dtos.TemaDTO;
import pt.ul.fc.css.services.CidadaoService;
import pt.ul.fc.css.services.ProjetoLeiService;
import pt.ul.fc.css.services.VotacaoService;

import java.time.LocalDateTime;


@SpringBootApplication
public class DemoApplication {

    private static final Logger log = LoggerFactory.getLogger(DemoApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    @Bean
    public CommandLineRunner demo(CidadaoService cidadaoService, ProjetoLeiService projetoLeiService, VotacaoService votacaoService) {

        return (args) -> {
            // save a few citizens
            CidadaoDTO c1 = cidadaoService.adicionaCidadao(1, "Joao", 1L);
            CidadaoDTO c2 = cidadaoService.adicionaCidadao(2, "Pedro", 2L);
            CidadaoDTO c3 = cidadaoService.adicionaCidadao(3, "Marcio", 3L);
            CidadaoDTO c4 = cidadaoService.adicionaCidadao(4, "Edmundo", 4L);
            CidadaoDTO c5 = cidadaoService.adicionaCidadao(5, "Jose", 5L);
            CidadaoDTO c6 = cidadaoService.adicionaCidadao(6, "Alfredo", 6L);
            CidadaoDTO c7 = cidadaoService.adicionaCidadao(7, "Antonio", 72342342L);
            CidadaoDTO c8 = cidadaoService.adicionaCidadao(8, "Mariana", 82343242L);
            CidadaoDTO c9 = cidadaoService.adicionaCidadao(9, "Fernanda", 9234234L);
            CidadaoDTO c10 = cidadaoService.adicionaCidadao(10, "Vanda", 4234234L);
            CidadaoDTO c11 = cidadaoService.adicionaCidadao(11, "Gisela", 5234234L);
            CidadaoDTO c12= cidadaoService.adicionaCidadao(12, "Concha", 6234324L);

            DelegadoDTO d1 = cidadaoService.apresentaComoDelegado(c1);
            DelegadoDTO d2 = cidadaoService.apresentaComoDelegado(c6);
            DelegadoDTO d3 = cidadaoService.apresentaComoDelegado(c11);


            TemaDTO tema1 = projetoLeiService.createTema("Educaçao");
            TemaDTO tema2 = projetoLeiService.createTema("Economia");
            TemaDTO tema3 = projetoLeiService.createTema("Desporto");
            TemaDTO tema4 = projetoLeiService.createTema("Turismo");


            ProjetoLeiDTO projetoLei = projetoLeiService.apresentarProjetoLei("Promoção da Educação de Qualidade e Igualdade",
                    "O projeto visa promover uma educação de qualidade, garantindo acesso igual para todos, formação adequada e infraestruturas adequadas nas escolas.",
                    "Como comer fruta".getBytes(), LocalDateTime.now().plusWeeks(4), d1, tema1);

            ProjetoLeiDTO projetoLei2 = projetoLeiService.apresentarProjetoLei("Fomento da Economia Sustentável e Inclusiva",
                    "O projeto visa promover a sustentabilidade econômica, incentivando a inclusão social e a preservação do meio ambiente.",
                    "Projeto2".getBytes(), LocalDateTime.now().plusWeeks(4), d1, tema2);

            ProjetoLeiDTO projetoLei3 = projetoLeiService.apresentarProjetoLei("Modernização e Desenvolvimento do Futebol Brasileiro",
                    "O projeto busca promover a modernização e o desenvolvimento do futebol brasileiro, com medidas para melhorar a gestão e infraestrutura desportiva.",
                    "Projeto3".getBytes(), LocalDateTime.now().plusWeeks(4), d2, tema3);

            ProjetoLeiDTO projetoLei4 = projetoLeiService.apresentarProjetoLei("Incentivo ao Turismo Sustentável e Desenvolvimento de Destinos Turísticos",
                    "O projeto visa incentivar o turismo sustentável, promovendo o desenvolvimento de destinos turísticos e preservação do patrimônio cultural e natural.",
                    "Como comer fruta".getBytes(), LocalDateTime.now().plusWeeks(4), d3, tema4);

            votacaoService.createVotacao(projetoLei.getId());
            votacaoService.createVotacao(projetoLei3.getId());
        };
    }
}
