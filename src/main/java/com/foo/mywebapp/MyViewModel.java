package com.foo.mywebapp;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.MouseEvent;
import org.zkoss.zul.Messagebox;

public class MyViewModel {

	private int count;

	private List<Contact> contacts;
	
	private Contact newcontact = new Contact();

//	private int selectedIndex;

	@Init
	public void init() throws ClassNotFoundException {
		contacts = new ArrayList<Contact>();
		/*
		Contact c = new Contact();
		c.setFio("Жиляев Артем Владимирович");
		c.setBirthday(LocalDate.parse("1976-02-20"));
		c.setPhone("8965-767-44-33");
		c.setAddress("г. Белгород ул. Ленина д.33 кв.125");
		contacts.add(c);
		c = new Contact();
		c.setFio("Прохор Артем Владимировчи");
		c.setBirthday(LocalDate.parse("1976-01-13"));
		c.setPhone("8951-367-42-11");
		c.setAddress("г. Старый Оскол ул. Пролетарская д.15 кв.16");
		contacts.add(c);*/
		
		contacts = DAO.init();
	}

	public List<Contact> getContacts() {
		return contacts;
	}

	@Command
	@NotifyChange("contacts")
	public void insert() {
		int id = DAO.insert(newcontact);
		newcontact.setId(id);
		contacts.add(newcontact);
		//clear form if success
		newcontact = new Contact();		
		BindUtils.postNotifyChange(null, null, this, "newcontact");
	}
	
	@Command
	@NotifyChange("contacts")
	public void update(@BindingParam("item") Contact con) {
		DAO.update(con);
		contacts.remove(con);//remove old by id
		contacts.add(con); //replace with new		
	}
	
	
	@Command	
	public void delete(@BindingParam("id") int id, @BindingParam("fio") String fio) {		 
		Messagebox.show("Удалить " + fio + "?", null,
				Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION,
		   new EventListener<Event>() {
			@Override
			public void onEvent(Event event) throws Exception {
				if (Messagebox.ON_OK.equals(event.getName())){
					DAO.delete(id);
					for (Iterator<Contact> it = contacts.iterator(); it.hasNext(); )			
						if (it.next().getId() == id)
							it.remove();
					
					BindUtils.postNotifyChange(null, null, MyViewModel.this, "contacts");
				}
			}
		   });
	}

	public Contact getNewcontact() {
		return newcontact;
	}

	public void setNewcontact(Contact newcontact) {
		this.newcontact = newcontact;
	}
	
	
}
