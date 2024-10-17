package com.tekdays

import grails.plugin.mail.MailService
import grails.transaction.Transactional
import groovy.sql.Sql

import javax.sql.DataSource

@Transactional
class TaskService {
MailService mailService
    DataSource dataSource
def  addDefaultTask(tekEvent){
    if (tekEvent.tasks?.size()>0)
        return
    tekEvent.addToTasks new Task(title:'Identify potential venues')
    tekEvent.addToTasks new Task(title:'Get price / availability of venues')
    tekEvent.addToTasks new Task(title:'Compile potential sponsor list')
    tekEvent.addToTasks new Task(title:'Design promotional materials')
    tekEvent.addToTasks new Task(title:'Compile potential advertising avenues')
    tekEvent.addToTasks new Task(title:'Locate swag provider (preferably local)')
    tekEvent.save()
}
    def notifyVolunteers(){
        def sql = new Sql(dataSource)
        String query="select tek_event_volunteers_id, tek_user_id from tek_event_tek_user "
        def result = sql.rows(query)
        sql.close()
        result.each{row->
            def eventId=row['tek_event_volunteers_id']
            def userId=row['tek_user_id']
            mailService.sendMail {

                to  TekUser?.findById(userId)?.email
                subject "Reminder for volunteers"
                body "Dear ${TekUser.get(userId)} this we are reminding you that the ${TekEvent.get(eventId)} wil start at ${TekEvent.get(eventId)?.startDate}. Thank u for volunteering"
            }


        }

    }
}
