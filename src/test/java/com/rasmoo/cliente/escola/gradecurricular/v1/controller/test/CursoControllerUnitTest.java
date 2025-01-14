package com.rasmoo.cliente.escola.gradecurricular.v1.controller.test;

import com.rasmoo.cliente.escola.gradecurricular.entity.CursoEntity;
import com.rasmoo.cliente.escola.gradecurricular.entity.MateriaEntity;
import com.rasmoo.cliente.escola.gradecurricular.v1.model.CursoModel;
import com.rasmoo.cliente.escola.gradecurricular.v1.model.Response;
import com.rasmoo.cliente.escola.gradecurricular.v1.service.ICursoService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(JUnitPlatform.class)
class CursoControllerUnitTest {

    @LocalServerPort
    private int port;

    @MockBean
    private ICursoService cursoService;

    @Autowired
    private TestRestTemplate restTemplate;

    private static CursoModel cursoModel;

    private static final String USER = "rasmoo";
    private static final String PASSWORD = "102030";

    @BeforeAll
    static void init() {

        cursoModel = new CursoModel();
        cursoModel.setId(1L);
        cursoModel.setCodCurso("ENGCP");
        cursoModel.setNome("ENGENHARIA DA COMPUTAÇÃO");

    }

    private String montaUri(String urn) {
        return "http://localhost:" + this.port + "/v1/curso/"+urn;
    }

    @Test
    void testListarCursos() {
        Mockito.when(this.cursoService.listar()).thenReturn(new ArrayList<CursoEntity>());

        ResponseEntity<Response<List<CursoEntity>>> cursos = restTemplate.withBasicAuth(USER, PASSWORD).exchange(
                this.montaUri(""), HttpMethod.GET, null,
                new ParameterizedTypeReference<Response<List<CursoEntity>>>() {
                });
        Assertions.assertNotNull(Objects.requireNonNull(cursos.getBody()).getData());
        assertEquals(200, cursos.getBody().getStatusCode());
    }

    @Test
    void testConsultarCurso() {

        CursoEntity curso = new CursoEntity();
        curso.setId(1L);
        curso.setCodigo("ENGCOMP");
        curso.setNome("ENGENHARIA DA COMPUTACAO");
        curso.setMaterias(new ArrayList<MateriaEntity>());
        Mockito.when(this.cursoService.consultarPorCodigo("ENGCOMP")).thenReturn(curso);

        ResponseEntity<Response<CursoEntity>> cursoResponse = restTemplate.withBasicAuth(USER, PASSWORD).exchange(
                this.montaUri("ENGCOMP"), HttpMethod.GET, null,
                new ParameterizedTypeReference<Response<CursoEntity>>() {
                });
        Assertions.assertNotNull(Objects.requireNonNull(cursoResponse.getBody()).getData());
        assertEquals(200, cursoResponse.getBody().getStatusCode());
    }

    @Test
    void testCadastrarCurso() {
        Mockito.when(this.cursoService.cadastrar(cursoModel)).thenReturn(Boolean.TRUE);

        cursoModel.setId(null);

        HttpEntity<CursoModel> request = new HttpEntity<>(cursoModel);

        ResponseEntity<Response<Boolean>> curso = restTemplate.withBasicAuth(USER, PASSWORD).exchange(
                this.montaUri(""), HttpMethod.POST, request,
                new ParameterizedTypeReference<Response<Boolean>>() {
                });
        Assertions.assertTrue(Objects.requireNonNull(curso.getBody()).getData());
        assertEquals(201, curso.getBody().getStatusCode());
        cursoModel.setId(1L);
    }

    @Test
    void testAtualizarCurso() {
        Mockito.when(this.cursoService.atualizar(cursoModel)).thenReturn(Boolean.TRUE);

        HttpEntity<CursoModel> request = new HttpEntity<>(cursoModel);

        ResponseEntity<Response<Boolean>> curso = restTemplate.withBasicAuth(USER, PASSWORD).exchange(
                this.montaUri(""), HttpMethod.PUT, request,
                new ParameterizedTypeReference<Response<Boolean>>() {
                });
        Assertions.assertTrue(Objects.requireNonNull(curso.getBody()).getData());
        assertEquals(200, curso.getBody().getStatusCode());
    }

    @Test
    void testExcluirCurso() {
        Mockito.when(this.cursoService.excluir(1L)).thenReturn(Boolean.TRUE);

        ResponseEntity<Response<Boolean>> curso = restTemplate.withBasicAuth(USER, PASSWORD).exchange(
                this.montaUri("1"), HttpMethod.DELETE, null,
                new ParameterizedTypeReference<Response<Boolean>>() {
                });
        Assertions.assertTrue(Objects.requireNonNull(curso.getBody()).getData());
        assertEquals(200, curso.getBody().getStatusCode());
    }

}