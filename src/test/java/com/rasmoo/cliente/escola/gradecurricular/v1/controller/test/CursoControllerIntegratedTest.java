package com.rasmoo.cliente.escola.gradecurricular.v1.controller.test;

import com.rasmoo.cliente.escola.gradecurricular.entity.CursoEntity;
import com.rasmoo.cliente.escola.gradecurricular.entity.MateriaEntity;
import com.rasmoo.cliente.escola.gradecurricular.v1.model.CursoModel;
import com.rasmoo.cliente.escola.gradecurricular.v1.model.Response;
import com.rasmoo.cliente.escola.gradecurricular.repository.ICursoRepository;
import com.rasmoo.cliente.escola.gradecurricular.repository.IMateriaRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(JUnitPlatform.class)
class CursoControllerIntegratedTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private IMateriaRepository materiaRepository;

    @Autowired
    private ICursoRepository cursoRepository;

    private static final String USER = "rasmoo";
    private static final String PASSWORD = "102030";

    @BeforeEach
    void init() {
        this.montaMateriaBaseDeDados();
        this.montaCursoBaseDeDados();
    }

    @AfterEach
    void finish() {
        this.cursoRepository.deleteAll();
        this.materiaRepository.deleteAll();
    }

    private String montaUri(String urn) {
        return "http://localhost:" + this.port + "/v1/curso/" + urn;
    }

    private void montaMateriaBaseDeDados() {
        MateriaEntity m1 = new MateriaEntity();
        m1.setCodigo("ILP");
        m1.setFrequencia(2);
        m1.setHoras(64);
        m1.setNome("INTRODUCAO A LINGUAGEM DE PROGRAMACAO");

        MateriaEntity m2 = new MateriaEntity();
        m2.setCodigo("POO");
        m2.setFrequencia(2);
        m2.setHoras(84);
        m2.setNome("PROGRAMACAO ORIENTADA A OBJETOS");

        MateriaEntity m3 = new MateriaEntity();
        m3.setCodigo("APA");
        m3.setFrequencia(1);
        m3.setHoras(102);
        m3.setNome("ANALISE E PROJETOS DE ALGORITMOS");

        this.materiaRepository.saveAll(Arrays.asList(m1, m2, m3));
    }

    private void montaCursoBaseDeDados() {

        List<MateriaEntity> listMaterias = this.materiaRepository.findAll();

        CursoEntity c1 = new CursoEntity();
        c1.setCodigo("ENGC");
        c1.setNome("ENGENHARIA DA COMPUTACAO");
        c1.setMaterias(listMaterias);

        this.cursoRepository.save(c1);
    }

    /*
     *
     * **********************CENÁRIOS DE SUCESSO************************
     *
     */

    @Test
    void testListarCursos() {

        ResponseEntity<Response<List<CursoEntity>>> cursos = restTemplate
                .withBasicAuth(USER, PASSWORD)
                .exchange(this.montaUri(""), HttpMethod.GET,
                null, new ParameterizedTypeReference<Response<List<CursoEntity>>>() {
                });
        Assertions.assertNotNull(Objects.requireNonNull(cursos.getBody()).getData());
        assertEquals(1, cursos.getBody().getData().size());
        assertEquals(200, cursos.getBody().getStatusCode());
    }

    @Test
    void testConsultarCursoPorCodigo() {

        ResponseEntity<Response<CursoEntity>> curso = restTemplate.withBasicAuth(USER, PASSWORD).
                exchange(this.montaUri("/ENGC"), HttpMethod.GET,
                null, new ParameterizedTypeReference<Response<CursoEntity>>() {
                });
        Assertions.assertNotNull(Objects.requireNonNull(curso.getBody()).getData());
        assertEquals("ENGENHARIA DA COMPUTACAO", curso.getBody().getData().getNome());
        assertEquals(200, curso.getBody().getStatusCode());
    }

    @Test
    void testAtualizarCurso() {

        List<CursoEntity> cursosList = this.cursoRepository.findAll();
        CursoEntity cursoEntity = cursosList.get(0);

        CursoModel cursoModel = new CursoModel();

        cursoModel.setId(cursoEntity.getId());
        cursoModel.setCodCurso(cursoEntity.getCodigo());
        cursoModel.setNome("Teste Atualiza curso");

        HttpEntity<CursoModel> request = new HttpEntity<>(cursoModel);

        ResponseEntity<Response<Boolean>> curso = restTemplate.withBasicAuth(USER, PASSWORD)
                .exchange(this.montaUri("/"), HttpMethod.PUT, request,
                new ParameterizedTypeReference<Response<Boolean>>() {
                });
        CursoEntity cursoAtualizado = this.cursoRepository.findCursoByCodigo(cursoEntity.getCodigo());

        Assertions.assertTrue(Objects.requireNonNull(curso.getBody()).getData());
        assertEquals("Teste Atualiza curso", cursoAtualizado.getNome());
        assertEquals(200, curso.getBody().getStatusCode());
    }

    @Test
    void testCadastrarCurso() {

        List<MateriaEntity> listMat = this.materiaRepository.findAll();
        List<Long> idListMat = new ArrayList<>();
        idListMat.add(listMat.get(0).getId());
        idListMat.add(listMat.get(1).getId());

        CursoModel cursoModel = new CursoModel();

        cursoModel.setCodCurso("CC");
        cursoModel.setNome("Ciências da computação");
        cursoModel.setMaterias(idListMat);

        HttpEntity<CursoModel> request = new HttpEntity<>(cursoModel);

        ResponseEntity<Response<Boolean>> curso = restTemplate.withBasicAuth(USER, PASSWORD)
                .exchange(this.montaUri(""), HttpMethod.POST, request,
                new ParameterizedTypeReference<Response<Boolean>>() {
                });
        List<CursoEntity> listCursoAtualizado = this.cursoRepository.findAll();

        Assertions.assertTrue(Objects.requireNonNull(curso.getBody()).getData());
        assertEquals(2, listCursoAtualizado.size());
        assertEquals(201, curso.getBody().getStatusCode());
    }

    @Test
    void testExcluirCursoPorId() {

        List<CursoEntity> materiaList = this.cursoRepository.findAll();
        Long id = materiaList.get(0).getId();

        ResponseEntity<Response<Boolean>> curso = restTemplate.withBasicAuth(USER, PASSWORD).
                exchange(this.montaUri(id.toString()), HttpMethod.DELETE,
                null, new ParameterizedTypeReference<Response<Boolean>>() {
                });

        List<CursoEntity> listCursoAtualizado = this.cursoRepository.findAll();

        Assertions.assertTrue(Objects.requireNonNull(curso.getBody()).getData());
        assertEquals(0, listCursoAtualizado.size());
        assertEquals(200, curso.getBody().getStatusCode());
    }

}