package com.ufape.jachei.service;

import com.ufape.jachei.models.PrestadorServico;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface InterfaceServicosPrestadorServicos {
    PrestadorServico savePrestadorServicos(PrestadorServico entity);

    PrestadorServico findByIdPrestadorServicos(Long id);

    List<PrestadorServico> findAllPrestadorServicos();

    List<PrestadorServico> findAllPrestadorServicosInCity(String city);

    List<PrestadorServico> findAllPrestadorServicosWhitService(@NotNull String service);

    void deleteByIdPrestadorServicos(Long id);

    void deletePrestadorServicos(PrestadorServico entity);
}
