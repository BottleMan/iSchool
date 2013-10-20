package org.ischool.config;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class XMLParase extends DefaultHandler
{
	
	//解析xml文件，采用sax解析
	private List<String> list;
	private String sortName;
	private boolean isItem;
	
	public XMLParase(String name)
	{
		this.sortName = name;
	}
	
	public List<String> getList()
	{
		return list;
	}
	
	@Override
	public void startDocument() throws SAXException
	{
		super.startDocument();
		list = new ArrayList<String>();
		isItem = false;
	}

	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException
	{
		super.startElement(uri, localName, qName, attributes);
		if (localName.equals("ClassRoom_"+sortName))
		{
			isItem = false;
			return;
		}
		if (localName.equals(sortName))
		{
			isItem = true;
			return;
		}
		isItem = false;
	}

	@Override
	public void characters(char[] ch, int start, int length) throws SAXException
	{
		super.characters(ch, start, length);
		String string = new String(ch, start, length);
		if(isItem)
		{
			list.add(string);
			isItem = false;
		}
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException
	{
		super.endElement(uri, localName, qName);
	}

	@Override
	public void endDocument() throws SAXException
	{
		super.endDocument();
	}

}
