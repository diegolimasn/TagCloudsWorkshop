package br.ufba.dcc.tagcloud.servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import br.ufba.dcc.tagcloud.Corpus;
import br.ufba.dcc.tagcloud.PopularityTagCloudCreator;
import br.ufba.dcc.tagcloud.SCTagCloudCreator;
import br.ufba.dcc.tagcloud.TagCloud;
import br.ufba.dcc.tagcloud.TagCloudAnalyzer;
import br.ufba.dcc.tagcloud.TagCloudCreator;
import br.ufba.dcc.tagcloud.TagCloudMain;
import br.ufba.dcc.tagcloud.TwitterCorpus;

public class TagCloudHome extends HttpServlet
{
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        response.getWriter().append("Served at: ").append(request.getContextPath());
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        //doGet(request, response);
        String twitterHandle = request.getParameter("twitterhandle");
        
        System.out.print(twitterHandle);
        //WebApplicationContext context = WebApplicationContextUtils.getRequiredWebApplicationContext(this.getServletContext());
        //Corpus corpus = (TwitterCorpus) context.getBean("corpus");
    }

}
