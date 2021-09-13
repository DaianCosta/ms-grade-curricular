package com.rasmoo.cliente.escola.gradecurricular.controller.test;

import com.rasmoo.cliente.escola.gradecurricular.dto.MateriaDto;
import com.rasmoo.cliente.escola.gradecurricular.model.Response;
import com.rasmoo.cliente.escola.gradecurricular.service.IMateriaService;
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
class MateriaControllerUnitTest {

    @LocalServerPort
    private int port;

    @MockBean
    private IMateriaService materiaService;

    @Autowired
    private TestRestTemplate restTemplate;

    private static MateriaDto materiaDto;

    @BeforeAll
    static void init() {

        materiaDto = new MateriaDto();
        materiaDto.setId(1L);
        materiaDto.setCodigo("ILP");
        materiaDto.setFrequencia(1);
        materiaDto.setHoras(64);
        materiaDto.setNome("INTRODUCAO A LINGUAGEM DE PROGRAMACAO");

    }

    @Test
    void testListarMaterias() {
        Mockito.when(this.materiaService.listar()).thenReturn(new ArrayList<MateriaDto>());

        ResponseEntity<Response<List<MateriaDto>>> materias = restTemplate.exchange(
                "http://localhost:" + this.port + "/materia/", HttpMethod.GET, null,
                new ParameterizedTypeReference<Response<List<MateriaDto>>>() {
                });
        Assertions.assertNotNull(Objects.requireNonNull(materias.getBody()).getData());
        assertEquals(200, materias.getBody().getStatusCode());
    }

    @Test
    void testConsultarMateria() {
        Mockito.when(this.materiaService.consultar(1L)).thenReturn(materiaDto);

        ResponseEntity<Response<MateriaDto>> materias = restTemplate.exchange(
                "http://localhost:" + this.port + "/materia/1", HttpMethod.GET, null,
                new ParameterizedTypeReference<Response<MateriaDto>>() {
                });
        Assertions.assertNotNull(Objects.requireNonNull(materias.getBody()).getData());
        assertEquals(200, materias.getBody().getStatusCode());
    }

    @Test
    void testCadastrarMateria() {
        Mockito.when(this.materiaService.cadastrar(materiaDto)).thenReturn(Boolean.TRUE);

        HttpEntity<MateriaDto> request = new HttpEntity<>(materiaDto);

        ResponseEntity<Response<Boolean>> materias = restTemplate.exchange(
                "http://localhost:" + this.port + "/materia/", HttpMethod.POST, request,
                new ParameterizedTypeReference<Response<Boolean>>() {
                });
        Assertions.assertTrue(Objects.requireNonNull(materias.getBody()).getData());
        assertEquals(201, materias.getBody().getStatusCode());
    }

    @Test
    void testAtualizarMateria() {
        Mockito.when(this.materiaService.atualizar(materiaDto)).thenReturn(Boolean.TRUE);

        HttpEntity<MateriaDto> request = new HttpEntity<>(materiaDto);

        ResponseEntity<Response<Boolean>> materias = restTemplate.exchange(
                "http://localhost:" + this.port + "/materia/", HttpMethod.PUT, request,
                new ParameterizedTypeReference<Response<Boolean>>() {
                });
        Assertions.assertTrue(Objects.requireNonNull(materias.getBody()).getData());
        assertEquals(200, materias.getBody().getStatusCode());
    }

    @Test
    void testExcluirMateria() {
        Mockito.when(this.materiaService.excluir(1L)).thenReturn(Boolean.TRUE);

        ResponseEntity<Response<Boolean>> materias = restTemplate.exchange(
                "http://localhost:" + this.port + "/materia/1", HttpMethod.DELETE, null,
                new ParameterizedTypeReference<Response<Boolean>>() {
                });
        Assertions.assertTrue(Objects.requireNonNull(materias.getBody()).getData());
        assertEquals(200, materias.getBody().getStatusCode());
    }

    @Test
    void testConsultarMateriasPorHoraMinima() {
        Mockito.when(this.materiaService.listarPorHorarioMinimo(64)).thenReturn(new ArrayList<MateriaDto>());

        ResponseEntity<Response<List<MateriaDto>>> materias = restTemplate.exchange(
                "http://localhost:" + this.port + "/materia/horario-minimo/64", HttpMethod.GET, null,
                new ParameterizedTypeReference<Response<List<MateriaDto>>>() {
                });
        Assertions.assertNotNull(Objects.requireNonNull(materias.getBody()).getData());
        assertEquals(200, materias.getBody().getStatusCode());
    }

    @Test
    void testConsultarMateriasPorFrequencia() {
        Mockito.when(this.materiaService.listarPorFrequencia(1)).thenReturn(new ArrayList<MateriaDto>());

        ResponseEntity<Response<List<MateriaDto>>> materias = restTemplate.exchange(
                "http://localhost:" + this.port + "/materia/frequencia/1", HttpMethod.GET, null,
                new ParameterizedTypeReference<Response<List<MateriaDto>>>() {
                });
        Assertions.assertNotNull(Objects.requireNonNull(materias.getBody()).getData());
        assertEquals(200, materias.getBody().getStatusCode());
    }

}