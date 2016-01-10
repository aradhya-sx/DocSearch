import MySQLdb
import MySQLdb.cursors
import random
from elasticsearch import Elasticsearch

es = Elasticsearch()#25#16

db = MySQLdb.connect(host = 'localhost',
        user = 'root',
        passwd = '',
        db = 'db100',
        cursorclass = MySQLdb.cursors.DictCursor)

cur = db.cursor()

cur.execute('SELECT id,name,recommendations from doctor ORDER BY id;');
doctors = cur.fetchall()

i = 1;
for doctor in doctors:
    doctorsjson = {}
    doctorsjson['id'] = doctor['id']
    doctorsjson['name'] = doctor['name']
    doctorsjson['recommendations'] = doctor['recommendations']
    cur.execute('SELECT specialityFK FROM doctorsspeciality WHERE doctorIdFK =%s',(doctor['id'],));
    specialities = cur.fetchall()
    sp = []
    for s in specialities:
        sp.append(s['specialityFK'])
    doctorsjson['specialities'] = sp    
    cur.execute('SELECT area FROm clinic WHERE id IN (SELECT clinicIdFK FROM doctorsclinic \
        WHERE doctorIdFK=%s)',(doctor['id'],))
    location = cur.fetchall()
    loc = []
    for lo in location:
        loc.append(lo['area'])
    doctorsjson['locations'] = loc
    es.index(index = 'doctors_search2',doc_type = 'items',id = i,body = {'doctors':doctorsjson})
    i = i+1
cur.close()
del cur
db.close()
