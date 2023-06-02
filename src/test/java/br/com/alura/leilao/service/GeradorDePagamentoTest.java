package br.com.alura.leilao.service;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import br.com.alura.leilao.dao.LeilaoDao;
import br.com.alura.leilao.dao.PagamentoDao;
import br.com.alura.leilao.model.Lance;
import br.com.alura.leilao.model.Leilao;
import br.com.alura.leilao.model.Pagamento;
import br.com.alura.leilao.model.Usuario;

class GeradorDePagamentoTest {
	
	@Mock
	private PagamentoDao dao;

	@Mock
	private GeradorDePagamento geradorDepagamento;
	
	@Mock
	private LeilaoDao leilaoDao;
	
	@Mock
	private Clock clock;
	
	@Captor
	private ArgumentCaptor<Pagamento> captor;
	
	
	@BeforeEach
	public void BeforeEach() {
		MockitoAnnotations.initMocks(this);
		this.geradorDepagamento = new GeradorDePagamento(dao);
	}

	@Test
	void testarGeradorDePagamentoSexta() {
		Leilao leilao = leilao();		
		Lance lanceVencedor = leilao.getLanceVencedor();
		
		LocalDate date = LocalDate.of(2020, 12, 7);
		
		Instant instant = date.atStartOfDay(ZoneId.systemDefault()).toInstant();
		
		Mockito.when(clock.instant()).thenReturn(instant);
		Mockito.when(clock.getZone()).thenReturn(ZoneId.systemDefault());
		
		geradorDepagamento.gerarPagamento(lanceVencedor);
		Mockito.verify(dao).salvar(captor.capture());
		
		Pagamento pagamento = captor.getValue();
		
		//Pagamento gerado na sexta busca o proximo dia util
		Assert.assertEquals(LocalDate.now().plusDays(3), pagamento.getVencimento());
		Assert.assertEquals(lanceVencedor.getValor(), pagamento.getValor());
		
		
	}	

	private Leilao leilao() {

        Leilao leilao = new Leilao("Celular",
                        new BigDecimal("500"),
                        new Usuario("Fulano"));

        Lance lance = new Lance(new Usuario("Beltrano"),
                        new BigDecimal("600"));
       

        leilao.propoe(lance);
        
        leilao.setLanceVencedor(lance);


        return leilao;

    }
}
