package dev.vality.proxy.mocketbank.apm.servlet;

import dev.vality.damsel.proxy_provider.ProviderProxySrv;
import dev.vality.woody.thrift.impl.http.THServiceBuilder;
import lombok.RequiredArgsConstructor;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import java.io.IOException;

@RequiredArgsConstructor
@WebServlet("/adapter/mocketbank/apm")
public class AdapterServletPayment extends GenericServlet {

    private final ProviderProxySrv.Iface serverHandler;
    private Servlet servlet;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        servlet = new THServiceBuilder()
                .build(ProviderProxySrv.Iface.class, serverHandler);
    }

    @Override
    public void service(ServletRequest request, ServletResponse response) throws ServletException, IOException {
        servlet.service(request, response);
    }
}
