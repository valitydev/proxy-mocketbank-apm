package dev.vality.proxy.mocketbank.apm.servlet;

import dev.vality.damsel.withdrawals.provider_adapter.AdapterSrv;
import dev.vality.woody.thrift.impl.http.THServiceBuilder;
import lombok.RequiredArgsConstructor;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import java.io.IOException;

@RequiredArgsConstructor
@WebServlet("/adapter/mocketbank/apm/payout")
public class AdapterServletPayout extends GenericServlet {

    private final AdapterSrv.Iface payoutAdapterServiceLogDecorator;
    private Servlet servlet;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        servlet = new THServiceBuilder()
                .build(AdapterSrv.Iface.class, payoutAdapterServiceLogDecorator);
    }

    @Override
    public void service(ServletRequest request, ServletResponse response) throws ServletException, IOException {
        servlet.service(request, response);
    }
}
