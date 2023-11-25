package pt.ul.fc.css.example.demo;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import pt.ul.fc.css.entities.Status;
import pt.ul.fc.css.entities.VotacaoResult;
import pt.ul.fc.css.entities.Voto;
import pt.ul.fc.css.facade.dtos.*;
import pt.ul.fc.css.facade.exceptions.*;
import pt.ul.fc.css.services.CidadaoService;
import pt.ul.fc.css.services.ProjetoLeiService;
import pt.ul.fc.css.services.VotacaoService;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class DemoApplicationTests {

    @Autowired
    ProjetoLeiService projetoLeiService;

    @Autowired
    VotacaoService votacaoService;

    @Autowired
    CidadaoService cidadaoService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @Order(1)
    public void testRestListarProjetosLei() throws Exception {

        CidadaoDTO c54 = cidadaoService.adicionaCidadao(54, "José", 40L);
        DelegadoDTO d54 = cidadaoService.apresentaComoDelegado(c54);
        TemaDTO tema1 = projetoLeiService.createTema("testes");

        projetoLeiService.apresentarProjetoLei("Frutaria", "Sobremesa saudavel",
                "Como comer fruta".getBytes(), LocalDateTime.now().plusWeeks(4), d54, tema1);

        projetoLeiService.apresentarProjetoLei("Fomentol da Economia Sustentável e Inclusiva",
                "O projeto visa promover a sustentabilidade econômica, incentivando a inclusão social e a preservação do meio ambiente.",
                "Projeto2".getBytes(), LocalDateTime.now().plusWeeks(4), d54, tema1);

        // testar pedido GET que devolve uma lista com todos os projetos de lei
        mockMvc.perform(get("/api/projetosLei"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(6)))// Demo Application ja coloca ProjetosLei na base de dados
                .andExpect(jsonPath("$[4].titulo").value("Frutaria"))
                .andExpect(jsonPath("$[5].titulo").value("Fomentol da Economia Sustentável e Inclusiva"));

    }

    @Test
    @Order(2)
    public void testRestConsultarProjetoLei() throws Exception {
        CidadaoDTO c540 = cidadaoService.adicionaCidadao(540, "José Carlos", 403L);
        DelegadoDTO d540 = cidadaoService.apresentaComoDelegado(c540);
        TemaDTO tema1 = projetoLeiService.createTema("testes2");

        ProjetoLeiDTO projeto3 = projetoLeiService.apresentarProjetoLei("Carrosel", "Tudo na feira",
                "palhaço".getBytes(), LocalDateTime.now().plusWeeks(1), d540, tema1);

        // testar pedido GET que devolve a informação de um projeto de lei com id {id}
        mockMvc.perform(get("/api/projetosLei/{id}", projeto3.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.titulo").value("Carrosel"))
                .andExpect(jsonPath("$.apoios").value(1));//apoios vem a 1 que corresponde ao apoio do delegado
    }


    @Test
    @Order(3)
    public void testRestListarVotacoes() throws Exception {

        CidadaoDTO c545 = cidadaoService.adicionaCidadao(545, "Josefina", 420L);
        DelegadoDTO d545 = cidadaoService.apresentaComoDelegado(c545);
        TemaDTO tema1 = projetoLeiService.createTema("testes3");

        ProjetoLeiDTO projeto = projetoLeiService.apresentarProjetoLei("Vegetais", "Nabiça mai barata",
                "tyuiop".getBytes(), LocalDateTime.now().plusWeeks(4), d545, tema1);

        votacaoService.createVotacao(projeto.getId());

        // testar pedido GET que devolve uma lista com todas as votações
        mockMvc.perform(get("/api/votacoes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))// Demo Application ja coloca Votacoes na base de dados
                .andExpect(jsonPath("$[2].projetoLei").value("Vegetais"));

    }

    @Test
    @Order(4)
    public void testRestConsultarVotacao() throws Exception {
        CidadaoDTO c5400 = cidadaoService.adicionaCidadao(5400, "Rey Mysterio", 4030L);
        DelegadoDTO d5400 = cidadaoService.apresentaComoDelegado(c5400);
        TemaDTO tema1 = projetoLeiService.createTema("testes4");

        ProjetoLeiDTO projeto = projetoLeiService.apresentarProjetoLei("Benfica", "38:)",
                "heehee".getBytes(), LocalDateTime.now().plusWeeks(1), d5400, tema1);

        VotacaoDTO votacao = votacaoService.createVotacao(projeto.getId());

        // testar pedido GET que devolve a informação de uma votacao com id {id}
        mockMvc.perform(get("/api/votacoes/{id}", votacao.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.projetoLei.titulo").value("Benfica"))
                .andExpect(jsonPath("$.votosAFavor").value(1))//votos a favor ja vem a um sendo este o voto do delegado
                .andExpect(jsonPath("$.votosContra").value(0));
    }


    @Test
    @Order(5)
    public void testRestApoiarProjetosLei() throws Exception {
        CidadaoDTO c127 = cidadaoService.adicionaCidadao(127, "Porfirio", 411203L);
        CidadaoDTO c1200 = cidadaoService.adicionaCidadao(1200, "Porfiria", 411202133L);
        DelegadoDTO d127 = cidadaoService.apresentaComoDelegado(c127);
        TemaDTO tema1 = projetoLeiService.createTema("testes5");

        ProjetoLeiDTO projeto = projetoLeiService.apresentarProjetoLei("Tinoni", "Alguem se magoou",
                "ambulancia".getBytes(), LocalDateTime.now().plusWeeks(1), d127, tema1);

        // testar pedido PUT que regista o apoio de um cidadao com id {ciadaoId} num projeto de lei com id {projetoLeiId}
        mockMvc.perform(put("/api/projetosLei/{projetoLeiId}/apoiaProjetoLei/{cidadaoId}", projeto.getId(), c1200.getId()))
                .andExpect(status().isOk());

        // testar pedido GET que devolve a informação de um projeto de lei com id {id}
        mockMvc.perform(get("/api/projetosLei/{id}", projeto.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.apoios").value(2));

    }

    @Test
    @Order(6)
    public void testRestCreateVotacao() throws Exception {
        CidadaoDTO c1312 = cidadaoService.adicionaCidadao(1312, "Porfirio", 411203L);
        DelegadoDTO d1312 = cidadaoService.apresentaComoDelegado(c1312);
        TemaDTO tema2 = projetoLeiService.createTema("testes9");

        ProjetoLeiDTO projeto = projetoLeiService.apresentarProjetoLei("Tinoniiii", "Alguem se magoou",
                "ambulancia".getBytes(), LocalDateTime.now().plusWeeks(1), d1312, tema2);


        mockMvc.perform(get("/api/votacoes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(4))); // size é 5 devido a criação de outras votações antes

        mockMvc.perform(post("/api/votacao/new/{projetoLeiId}", projeto.getId()))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/votacoes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(5)));// size aumenta um depois do post
    }


    @Test
    @Order(7)
    public void testRestVotarVotacao() throws Exception {
        CidadaoDTO c5400123 = cidadaoService.adicionaCidadao(5400123, "Jay z", 4030L);
        CidadaoDTO c54123 = cidadaoService.adicionaCidadao(54123, "Kanye", 4030L);
        CidadaoDTO c5123 = cidadaoService.adicionaCidadao(5123, "DaBaby", 4030L);
        DelegadoDTO d5400123 = cidadaoService.apresentaComoDelegado(c5400123);
        TemaDTO tema1 = projetoLeiService.createTema("testes6");

        ProjetoLeiDTO projeto = projetoLeiService.apresentarProjetoLei("Crista Ronaldo", "Sewey",
                "SIUUUUUU".getBytes(), LocalDateTime.now().plusWeeks(1), d5400123, tema1);

        VotacaoDTO votacao = votacaoService.createVotacao(projeto.getId());

        // testar pedido PUT que regista o voto {voto} do cidadao com id {cidadaoId} na votacao com id {votacaoId}
        mockMvc.perform(put("/api/votacoes/{votacaoId}/votarVotacao/{cidadaoId}/{voto}", votacao.getId(),c54123.getId(), Voto.FAVOR.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.votosAFavor").value(2));//votos a favor ja vem a um sendo este o voto do delegado

        // testar pedido PUT que regista o voto {voto} do cidadao com id {cidadaoId} na votacao com id {votacaoId}
        mockMvc.perform(put("/api/votacoes/{votacaoId}/votarVotacao/{cidadaoId}/{voto}", votacao.getId(),c5123.getId(), Voto.CONTRA.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.votosContra").value(1));

    }

    @Test
    @Order(8)
    public void testRestLogin() throws Exception {
        CidadaoDTO c312 = cidadaoService.adicionaCidadao(312, "Z Jay", 41310L);
        int cidadaoCC = c312.getNrCC();
        Long cidadaoId = c312.getId();

        // testar pedido GET que devolve o id associado ao nrCC dado
        mockMvc.perform(get("/api/login/{nrCC}", cidadaoCC))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(cidadaoId));
    }

    @Test
    public void testAdicionarEntidades() throws ApplicationException, TemaNotFoundException {

        // adicionar cidadao e apresenta-lo como delegado
        CidadaoDTO c30 = cidadaoService.adicionaCidadao(30, "Pedro", 40L);
        Optional<CidadaoDTO> cidadao = cidadaoService.getCidadao(c30.getId());
        if (cidadao.isPresent()) {
            // cidadao com nrCC = 30 apresenta-se como delegado
            DelegadoDTO delegado = cidadaoService.apresentaComoDelegado(cidadao.get());

            assertNotNull(cidadao);
            assertNotNull(delegado);

            // criar temas e compara-los
            TemaDTO tema1 = projetoLeiService.createTema("banana");
            TemaDTO tema2 = projetoLeiService.createTema("laranja");

            assertNotEquals(tema1, tema2);
            assertNotNull(projetoLeiService.findTemaByDesignacao("laranja"));
        }
    }

    @Test
    public void apresentarProjetoLei() throws ApplicationException {

        Optional<CidadaoDTO> cidadao = cidadaoService.getCidadao(2);

        if (cidadao.isPresent()) {
            // cidadao com nrCC = 1 apresenta-se como delegado
            DelegadoDTO delegado = cidadaoService.apresentaComoDelegado(cidadao.get());

            //criar tema
            TemaDTO tema = projetoLeiService.createTema("bacalhau");

            assertNotNull(delegado);
            assertNotNull(tema);

            // delegado com nrCC = 1 apresenta um projeto de lei
            ProjetoLeiDTO projetoLei = projetoLeiService.apresentarProjetoLei("Peixe", "Sobremesa saudavel",
                    "Como comer fruta".getBytes(), LocalDateTime.now().plusWeeks(4), delegado, tema);

            Long id = projetoLei.getId();
            Optional<ProjetoLeiDTO> op = projetoLeiService.getProjetoLei(id);    // findById usa o metodo getReferenceById

            // not equals porque nao carrega a informacao para o projeto2
            op.ifPresent(projetoLei2 -> assertNotEquals(projetoLei, projetoLei2));
        }
    }

    @Test
    public void fecharProjetoLeiExpirados() throws ApplicationException, ProjetoLeiNotFoundException {

        Optional<CidadaoDTO> cidadao = cidadaoService.getCidadao(7);
        if (cidadao.isPresent()) {
            // cidadao com nrCC = 2 apresenta-se como delegado
            DelegadoDTO delegado = cidadaoService.apresentaComoDelegado(cidadao.get());

            //criar tema
            TemaDTO tema = projetoLeiService.createTema("Bicho-Pau");

            assertNotNull(delegado);
            assertNotNull(tema);

            // delegado com nrCC = 2 apresenta um projeto de lei
            // se for apresentado um projetoLei com a data invalida é levanta uma exceção!
            ProjetoLeiDTO projeto1 = projetoLeiService.apresentarProjetoLei("Animais", "Insetos curiosos",
                    "Como encontrar bicho pau".getBytes(), LocalDateTime.now().plusHours(50), delegado, tema);

            // alterar a data de validade do projeto1 para uma data expirada
            projetoLeiService.updateDataLimite(projeto1.getId(), LocalDateTime.now().minusDays(2));

            Optional<ProjetoLeiDTO> projeto2 = projetoLeiService.getProjetoLei(projeto1.getId());

            if (projeto2.isPresent()) {
                assertNotNull(projeto1.getDataLimite());
                assertNotNull(projeto2.get().getDataLimite());

                // uma data so eh valida se estiver entre hoje e hoje.plusYears(1)
                assertFalse(projeto2.get().isValidDate(projeto2.get().getDataLimite()));
                assertEquals(Status.ABERTO, projeto1.getStatus());      // pre fecharProjetosLeiExpirados

                // testar fechar o projeto1, visto que tem uma data expirada
                projetoLeiService.fecharProjetosLeiExpirados();
                Optional<ProjetoLeiDTO> projeto3 = projetoLeiService.getProjetoLei(projeto1.getId());
                // pos fecharProjetosLeiExpirados
                projeto3.ifPresent(projetoLeiDTO -> assertEquals(Status.FECHADO, projetoLeiDTO.getStatus()));
            }
        }
    }

    @Test
    public void consultarProjetosLei() throws ApplicationException{

        Optional<CidadaoDTO> cidadao = cidadaoService.getCidadao(3);
        if (cidadao.isPresent()) {
            // cidadao com nrCC = 3 apresenta-se como delegado
            DelegadoDTO delegado = cidadaoService.apresentaComoDelegado(cidadao.get());

            //criar tema
            TemaDTO tema = projetoLeiService.createTema("Vida");

            // delegado com nrCC = 3 apresenta um projeto de lei
            ProjetoLeiDTO projetoLei = projetoLeiService.apresentarProjetoLei("Pavões", "Demasiado ruído causado por dadas aves",
                    "Como afugentar pavão".getBytes(), LocalDateTime.now().plusHours(50).truncatedTo(ChronoUnit.SECONDS), delegado, tema);

            // testar se todos os dados do projetoLei estao corretos
            String plToString = "ProjetoLei: \n" +
                    "-> id = " + projetoLei.getId() + "\n" +
                    "-> titulo = " + projetoLei.getTitulo() + "\n" +
                    "-> descricao = " + projetoLei.getDescricao() + "\n" +
                    "-> anexoPdf = " + Arrays.toString(projetoLei.getAnexoPDF()) + "\n" +
                    "-> data = " + projetoLei.getDataLimite() + "\n" +
                    "-> delegado = " + projetoLei.getDelegado().getNome() + "\n" +
                    "-> tema = " + projetoLei.getTema().getDesignacao() + "\n" +
                    "-> apoios = " + projetoLei.getApoios() + "\n" +
                    "-> status = " + projetoLei.getStatus().toString();
            assertEquals(plToString, projetoLeiService.consultarProjetoLei(projetoLei.getId()));
        }
    }

    @Test
    public void listarProjetosLei() throws ApplicationException, ProjetoLeiNotFoundException {

        //fechar todos os projetos de lei que tenham sido criados antes
        List<ProjetoLeiDTO> listaPLsJaExistentes = projetoLeiService.listarProjetosLeiAbertos();
        for (ProjetoLeiDTO pl : listaPLsJaExistentes) {
            projetoLeiService.updateDataLimite(pl.getId(), LocalDateTime.now().minusDays(1));
        }
        projetoLeiService.fecharProjetosLeiExpirados();

        Optional<CidadaoDTO> cidadao4 = cidadaoService.getCidadao(4);
        Optional<CidadaoDTO> cidadao5 = cidadaoService.getCidadao(5);

        if (cidadao4.isPresent() && cidadao5.isPresent()) {
            // apresentar novos delegados
            DelegadoDTO delegado4 = cidadaoService.apresentaComoDelegado(cidadao4.get());
            DelegadoDTO delegado5 = cidadaoService.apresentaComoDelegado(cidadao5.get());

            // criar temas
            TemaDTO tema1 = projetoLeiService.createTema("Ensino");
            TemaDTO tema2 = projetoLeiService.createTema("Economics");

            // apresentar projetos de lei
            ProjetoLeiDTO projeto1 = projetoLeiService.apresentarProjetoLei("Exames", "Abolicao de epoca de exames",
                    "Abolicao de epoca de exames".getBytes(), LocalDateTime.now().plusWeeks(3), delegado4, tema1);
            ProjetoLeiDTO projeto2 = projetoLeiService.apresentarProjetoLei("Aulas", "Trazer de volta teoricas online",
                    "Trazer de volta teoricas online".getBytes(), LocalDateTime.now().plusMonths(3), delegado5, tema1);
            ProjetoLeiDTO projeto3 = projetoLeiService.apresentarProjetoLei("PasseSub23", "Tornar passe gratis para estudantes",
                    "Tornar passe gratis para estudantes".getBytes(), LocalDateTime.now().plusDays(5), delegado5, tema2);

            // testar se os dados dos projetos de lei estao corretos
            assertNotEquals(projeto1.getDelegado().getNome(), projeto2.getDelegado().getNome());
            assertEquals(projeto1.getTema().getDesignacao(), projeto2.getTema().getDesignacao());

            List<ProjetoLeiDTO> listaProjetosLei = projetoLeiService.listarProjetosLeiAbertos();
            assertEquals(projeto1.getId(), listaProjetosLei.get(0).getId());

            // O delegado nao eh igual porque eh uma entity
            assertNotEquals(projeto2.getDelegado(), listaProjetosLei.get(1).getDelegado());

            // mas a informacao eh a mesma
            assertEquals(projeto3.getDelegado().getId(), listaProjetosLei.get(2).getDelegado().getId());
        }
    }

    @Test
    public void apoiarProjetoLei() throws ApplicationException, ProjetoLeiNotFoundException {

        // adicionar cidadaos
        CidadaoDTO cidadao1 = cidadaoService.adicionaCidadao(60, "Tania", 40L);
        CidadaoDTO cidadao2 = cidadaoService.adicionaCidadao(70, "Josefa", 50L);

        // apresenta-los como delegados
        DelegadoDTO delegado6 = cidadaoService.apresentaComoDelegado(cidadao1);
        DelegadoDTO delegado7 = cidadaoService.apresentaComoDelegado(cidadao2);

        // criar temas
        TemaDTO tema3 = projetoLeiService.createTema("Futxibol");
        TemaDTO tema4 = projetoLeiService.createTema("PirataInformatico");

        // apresentar projetos de lei
        ProjetoLeiDTO projeto1 = projetoLeiService.apresentarProjetoLei("Tempo", "Parar o relogio quando a bola sai fora",
                "Parar o relogio quando a bola sai fora".getBytes(), LocalDateTime.now().plusWeeks(3), delegado6, tema3);
        ProjetoLeiDTO projeto2 = projetoLeiService.apresentarProjetoLei("Regras", "Ban 6 meses apos simulacao de falta",
                "Ban 6 meses apos simulacao de falta".getBytes(), LocalDateTime.now().plusMonths(2), delegado7, tema3);
        ProjetoLeiDTO projeto3 = projetoLeiService.apresentarProjetoLei("Hackear WiFi", "Tornar legal roubar internet ao vizinho",
                "Tornar legal roubar internet ao vizinho".getBytes(), LocalDateTime.now().plusWeeks(5), delegado7, tema4);

        // adicionar cidadaos para apoiarem os projetos de lei
        CidadaoDTO cidadao12 = cidadaoService.adicionaCidadao(120, "Alfredo", 400L);
        CidadaoDTO cidadao34 = cidadaoService.adicionaCidadao(34, "Sonica", 4120L);
        CidadaoDTO cidadao45 = cidadaoService.adicionaCidadao(45, "Lil Poldina", 890L);
        CidadaoDTO cidadao112 = cidadaoService.adicionaCidadao(112, "Popotimus", 122L);
        CidadaoDTO cidadao777 = cidadaoService.adicionaCidadao(777, "Pumba", 56L);

        // apoiar os projetos de lei
        projetoLeiService.apoia(projeto1.getId(), cidadao12.getId());
        projetoLeiService.apoia(projeto3.getId(), cidadao12.getId());
        projetoLeiService.apoia(projeto2.getId(), cidadao34.getId());
        projetoLeiService.apoia(projeto1.getId(), cidadao45.getId());
        projetoLeiService.apoia(projeto2.getId(), cidadao112.getId());

        Optional<ProjetoLeiDTO> projeto4 = projetoLeiService.getProjetoLei(projeto1.getId());
        Optional<ProjetoLeiDTO> projeto5 = projetoLeiService.getProjetoLei(projeto2.getId());
        Optional<ProjetoLeiDTO> projeto6 = projetoLeiService.getProjetoLei(projeto3.getId());

        if (projeto4.isPresent() && projeto5.isPresent() && projeto6.isPresent()) {

            // verificar numero de apoios de cada projeto de lei
            assertEquals(3, projeto4.get().getApoios());
            assertEquals(3, projeto5.get().getApoios());
            assertEquals(2, projeto6.get().getApoios());

            // alterar os apoios para proximo apoio criar uma votacao
            projetoLeiService.updateApoios(projeto6.get().getId(), 10000);

            // votacao eh criada pois nrApoios >= 10000
            projetoLeiService.apoia(projeto6.get().getId(), cidadao777.getId());
            votacaoService.createVotacao(projeto6.get().getId());

            // verificar se a votacao foi criada
            assertTrue(votacaoService.existsVotacao(projeto6.get().getId()));
        }
    }

    @Test
    public void escolherDelegado() throws CidadaoNotFoundException, ApplicationException {

        // adicionar cidadaos
        CidadaoDTO cidadao = cidadaoService.adicionaCidadao(1000,"El mano", 898L);
        CidadaoDTO delegado = cidadaoService.adicionaCidadao(800,"Divanildo", 8888L);

        // apresentar como delegado
        cidadaoService.apresentaComoDelegado(delegado);

        // criar temas
        TemaDTO tema = projetoLeiService.createTema("Unicornos");

        // cidadao escolhe (associa) delegado para tema
        cidadaoService.escolheDelegado(delegado.getId(), cidadao.getId(), tema.getDesignacao());
    }

    @Test
    public void votarNumaProposta() throws ApplicationException, ProjetoLeiNotFoundException {

        // adicionar cidadao e apreentar como delegado
        CidadaoDTO cidadao555 = cidadaoService.adicionaCidadao(555,"Catia Vanessa", 88348L);
        CidadaoDTO cidadao666 = cidadaoService.adicionaCidadao(666, "Jessica Soraia", 56778L);

        // apresentar cidadao com nrCC = 555 como delegado
        DelegadoDTO delegado = cidadaoService.apresentaComoDelegado(cidadao555);

        // criar tema
        TemaDTO tema5 = projetoLeiService.createTema("OliverEBenji");

        // apresentar projeto de lei
        ProjetoLeiDTO projeto = projetoLeiService.apresentarProjetoLei("Magos da Bola", "Benji Oliver, vão ser os campeoes",
                "Remate colocado".getBytes(), LocalDateTime.now().plusWeeks(5), delegado, tema5);

        // verificar se o projeto de lei, ao ser apresentado, conta logo a partida com um apoio (delegado proponente)
        assertEquals(1, projeto.getApoios());

        // alterar os apoios para proximo apoio criar uma votacao
        projetoLeiService.updateApoios(projeto.getId(), 10000);

        // votacao criada
        projetoLeiService.apoia(projeto.getId(),cidadao666.getId());
        VotacaoDTO vdto = votacaoService.createVotacao(projeto.getId());
        assertTrue(votacaoService.existsVotacao(projeto.getId()));

        // votar a favor na votacao e verificar que o voto não é registado pois foi este cidadao que
        // apresentou o projeto de lei (e consequentemente é o delegado proponente da votação)
        votacaoService.votarVotacao(vdto.getId(), cidadao555.getId(), Voto.FAVOR);
        assertEquals(1,votacaoService.getVotacao(vdto.getId()).get().getVotosAFavor());

        // votar contra na votacao e verificar se o voto ficou registado corretamente
        votacaoService.votarVotacao(vdto.getId(), cidadao666.getId(), Voto.CONTRA);
        assertEquals(1,votacaoService.getVotacao(vdto.getId()).get().getVotosContra());
    }

    @Test
    public void fecharUmaVotacao() throws ApplicationException, ProjetoLeiNotFoundException, VotacaoNotFoundException {

        // adicionar cidadao
        CidadaoDTO cidadao999 = cidadaoService.adicionaCidadao(999,"Vanda Paula", 13L);
        CidadaoDTO cidadao5454 = cidadaoService.adicionaCidadao(5454,"Monica Nicole", 3456789L);

        // apresentar como delegado
        DelegadoDTO delegado = cidadaoService.apresentaComoDelegado(cidadao999);

        // criar tema
        TemaDTO tema6 = projetoLeiService.createTema("Cozinha");

        // apresentar projeto de lei
        ProjetoLeiDTO projeto = projetoLeiService.apresentarProjetoLei("Exaustor", "Sera assim mais facil baixar a chinfrineira",
                "FFFFFFFFF".getBytes(), LocalDateTime.now().plusWeeks(5), delegado, tema6);

        // alterar os apoios para proximo apoio criar uma votacao
        projetoLeiService.updateApoios(projeto.getId(), 10000);

        // votacao criada
        projetoLeiService.apoia(projeto.getId(), cidadao5454.getId());
        votacaoService.createVotacao(projeto.getId());

        // votar a favor na votacao para a votacao ter apenas 1 voto a favor e 0 contra
        votacaoService.votarVotacao(projeto.getId(), cidadao5454.getId(), Voto.FAVOR);

        // alterar a data de validade da votacao para uma data expirada
        votacaoService.updateDataLimite(votacaoService.findByProjetoLeiId(projeto.getId()).getId(),
                LocalDateTime.now().minusMonths(1));

        //fechar a votacao agora que ja tem uma data expirada
        votacaoService.fecharVotacoes();

        VotacaoDTO votacao = votacaoService.findByProjetoLeiId(projeto.getId());

        // verificar se o status da votacao passou para FECHADO e se o resultado eh APROVADA
        // dado que a votacao tem apenas 1 voto a favor e 0 contra
        assertEquals(Status.FECHADO, votacao.getStatus());
        assertEquals(VotacaoResult.APROVADA, votacao.getVotacaoResult());
    }

    @Test
    public void democracia2ExampleTest() throws ApplicationException, CidadaoNotFoundException,
            ProjetoLeiNotFoundException, VotacaoNotFoundException {

        // adicionar cidadaos
        CidadaoDTO cidadao100 = cidadaoService.adicionaCidadao(100,"Tiago", 41L);
        CidadaoDTO cidadao101 = cidadaoService.adicionaCidadao(101,"Ivo", 401L);
        CidadaoDTO cidadao102 = cidadaoService.adicionaCidadao(102,"Carlos", 4011L);
        CidadaoDTO cidadao103 = cidadaoService.adicionaCidadao(103,"Cristiano", 40111L);
        CidadaoDTO cidadao104 = cidadaoService.adicionaCidadao(104,"Ricardo", 401111L);
        CidadaoDTO cidadao105 = cidadaoService.adicionaCidadao(105,"Pepe", 4011111L);
        CidadaoDTO cidadao106 = cidadaoService.adicionaCidadao(106,"Quaresma", 40111111L);

        //apresentar como delegados
        DelegadoDTO d102 = cidadaoService.apresentaComoDelegado(cidadao102);
        DelegadoDTO d105 = cidadaoService.apresentaComoDelegado(cidadao105);
        DelegadoDTO d106 = cidadaoService.apresentaComoDelegado(cidadao106);

        //fechar projetos de lei que tenham sido criados antes
        List<ProjetoLeiDTO> listaPLsJaExistentes = projetoLeiService.listarProjetosLeiAbertos();
        for (ProjetoLeiDTO pl : listaPLsJaExistentes) {
            projetoLeiService.updateDataLimite(pl.getId(), LocalDateTime.now().minusDays(1));
        }
        projetoLeiService.fecharProjetosLeiExpirados();

        // criar temas
        TemaDTO tema7 = projetoLeiService.createTema("Luz");
        TemaDTO tema8 = projetoLeiService.createTema("Som");
        TemaDTO tema9 = projetoLeiService.createTema("Natureza");

        // apresentar projetos de lei
        ProjetoLeiDTO projeto1 = projetoLeiService.apresentarProjetoLei("Brilho do Sol", "Raios uv perigosos",
                "AAAAAAAAAA".getBytes(), LocalDateTime.now().plusWeeks(1), d102, tema7);

        ProjetoLeiDTO projeto2 = projetoLeiService.apresentarProjetoLei("Poluição sonora", "Permitir buzinas a noite",
                "BBBBBBBB".getBytes(), LocalDateTime.now().plusDays(8), d105, tema8);

        ProjetoLeiDTO projeto3 = projetoLeiService.apresentarProjetoLei("Arvores", "Plantar arvores sustentara o globo",
                "CCCCCCCCCCCCCCCC".getBytes(), LocalDateTime.now().plusWeeks(40), d105, tema9);

        // alterar a data de validade da votacao para uma data expirada
        projetoLeiService.updateDataLimite(projeto3.getId(), LocalDateTime.now().minusDays(1));

        // testar se o projeto de lei com data expirada eh fechado (3 projetos adicionados, mas size() = 2)
        projetoLeiService.fecharProjetosLeiExpirados();
        assertEquals(2,projetoLeiService.listarProjetosLeiAbertos().size());

        // apoiar projetos de lei
        projetoLeiService.apoia(projeto1.getId(), cidadao100.getId());
        projetoLeiService.apoia(projeto1.getId(), cidadao101.getId());
        projetoLeiService.apoia(projeto1.getId(), cidadao101.getId()); // apoia a dobrar

        // testar se quando se apoia um projeto de lei outra vez, o apoio nao eh registado
        // (2 apoios + apoio do delegado proponente)
        Optional<ProjetoLeiDTO> projeto = projetoLeiService.getProjetoLei(projeto1.getId());
        projeto.ifPresent(projetoLeiDTO -> assertEquals(3, projetoLeiDTO.getApoios()));

        // alterar os apoios para proximo apoio criar uma votacao
        projetoLeiService.updateApoios(projeto2.getId(),10000);

        // votacao criada
        projetoLeiService.apoia(projeto2.getId(), cidadao103.getId());
        VotacaoDTO votacao1 = votacaoService.createVotacao(projeto2.getId());

        // verificacao se a votacao foi criada corretamente
        assertNotNull(votacaoService.findByProjetoLeiId(projeto2.getId()));


        // verificar se o limite minimo da votacao eh alterado quando a validade do projeto de lei
        // eh menor do que 15 dias (altera para 15 if < 15)
        assertEquals(LocalDateTime.now().plusDays(15).getDayOfMonth(), votacao1.getDataFecho().getDayOfMonth());

        // cidadaos escolhem (associa) delegados para temas
        cidadaoService.escolheDelegado(d102.getId(), cidadao104.getId(), tema8.getDesignacao());
        cidadaoService.escolheDelegado(d102.getId(), cidadao100.getId(), tema8.getDesignacao());
        cidadaoService.escolheDelegado(d106.getId(), cidadao101.getId(), tema8.getDesignacao());
        cidadaoService.escolheDelegado(d102.getId(), cidadao103.getId(), tema8.getDesignacao());

        //delegados votam numa votacao
        votacaoService.votarVotacao(votacao1.getId(), cidadao102.getId(), Voto.FAVOR);
        votacaoService.votarVotacao(votacao1.getId(), cidadao106.getId(), Voto.CONTRA);

        // verificar se o voto do delegado foi registado corretamente
        assertEquals(2, votacaoService.getVotacao(votacao1.getId()).get().getVotosAFavor());

        // alterar a data de validade da votacao para uma data expirada e fechar a votacao
        votacaoService.updateDataLimite(votacao1.getId(),LocalDateTime.now().minusMonths(1));

        // fechar a votacao agora que ja tem uma data expirada
        votacaoService.fecharVotacoes();

        VotacaoDTO votacao2 = votacaoService.findByProjetoLeiId(projeto2.getId());

        // testar se o status da votacao foi alterado corretamente
        assertEquals(Status.FECHADO, votacao2.getStatus());

        // verificar se os votos estao corretos
        assertEquals(5, votacao2.getVotosAFavor());
        assertEquals(2, votacao2.getVotosContra());

        // verificar se o resultado da votacao foi definido corretamente
        assertEquals(VotacaoResult.APROVADA, votacao2.getVotacaoResult());
    }
}
