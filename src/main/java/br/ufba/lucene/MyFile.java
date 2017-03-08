/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufba.lucene;

/**
 *
 * @author felipe
 */
public class MyFile {
    private String name;
    private String url;
    private String content;

    public MyFile(String name, String url, String content)
    {
        this.name = name;
        this.url = url;
        this.content = content;
    }

    public MyFile(String name, String content)
    {
        this.name = name;
        this.url = name;
        this.content = content;
    }

    public String getName()
    {
        return name;
    }

    public String getContent()
    {
        return content;
    }

    public void setUrl(String url)
    {
        this.url = url;
    }

    public String getUrl()
    {
        return url;
    }
}