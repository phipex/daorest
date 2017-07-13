package co.com.ies.test.pdanyos.service;

import co.com.ies.test.pdanyos.repository.AbstractRestRepository;
import co.com.ies.test.pdanyos.service.impl.RemoteLoginServiceImpl;

public interface RemoteLoginService {
    boolean loginRemoto(AbstractRestRepository.CredencialesRest credencialesRest, RemoteLoginServiceImpl.Login login);
}
