from flask import Flask, render_template, request, session, redirect, url_for, jsonify
from database import init_db, db_session 
from models import *
from forms import *
from sqlalchemy import and_
import json
from elasticsearch import Elasticsearch
from elasticsearch_dsl import Search, Q
from elasticsearch_dsl.query import MultiMatch

init_db()
app = Flask('app')


@app.route('/elastic/<int:stpage>/<location>/<speciality>', methods = ['GET', 'POST'])
def elasticData(stpage, location, speciality):
    location = location
    speciality = speciality
    es = Elasticsearch()
    s = Search(es)
    dictlocation = { "doctors.locations" : location}
    dictspeciality = { "doctors.specialities" : speciality }
    q = Q("match", **dictlocation) & Q("match", **dictspeciality)
    hits = s.query(q).sort().extra(from_=stpage, size=5).execute()
    hits = hits.to_dict()
    list = []
    for hit in hits['hits']['hits']:
          list.append(hit['_source']['doctors'])
    return json.dumps(list)

@app.route('/', methods = ['GET', 'POST'])
def home():
    return app.send_static_file('index.html')

@app.route('/<path:path>')
def staticProxy(path):
  return app.send_static_file(path)

@app.route('/addDoctor', methods = ['POST'])
def addDoctor():
    docJson = request.get_json()
    doctorDetails = Doctor(docJson['name'], 
        docJson['email'], 
        docJson['experience'],
        docJson['education'], 
        docJson['recommendations'])
    specList = docJson['speclist']
    clinicList = docJson['cliniclist']
    for specialityName in specList:
        speciality = Speciality.query.filter_by(name = specialityName['name']).first()
        doctorDetails.speciality.append(speciality)
    for clinicName in clinicList:
        clinic = Clinic.query.filter_by(name = clinicName['name']).first()
        doctorDetails.clinics.append(clinic)
    db_session.add(doctorDetails)
    try:
        db_session.commit()
    except SQLAlchemyError as exception:
        db_session.rollback()
    page = {'returnCode': "SUCCESS", 'data':{}, 'errorCode':None}
    return jsonify(page)

@app.route('/editDoctor', methods=['POST'])
def editDoctor():
    doctorJson = request.get_json()
    print doctorJson
    db_session.query(Doctor).filter_by(id = doctorJson['id']).update({
        'name' : doctorJson['name'],
        'email' : doctorJson['email'], 
        'experience' : doctorJson['experience'],
        'education' : doctorJson['education'], 
        'recommendations' : doctorJson['recommendations']
    })
    specList = doctorJson['speclist']
    specDelete = DoctorsSpeciality.query.filter_by(doctorIdFK = doctorJson['id'])
    for specdel in specDelete:
        del specdel
        db_session.commit()
    docObj = Doctor.query.get(doctorJson['id'])
    for specialityName in specList:
        print specialityName['specName']
        speciality = Speciality.query.filter_by(name = specialityName['specName']).first()
        docObj.speciality.append(speciality)
        db_session.commit()


    clinicList = doctorJson['cliniclist']
    clinicDelete = DoctorsClinic.query.filter_by(doctorIdFK = doctorJson['id'])
    for clinicdel in clinicDelete:
        del clinicdel
        db_session.commit()
    for clinicName in clinicList:
        clinic = Clinic.query.filter_by(name = clinicName['clinicName']).first()
        docObj.clinics.append(clinic)
        db_session.commit()

    try:
        db_session.commit()
    except SQLAlchemyError as exception:
        db_session.rollback()
    page = {'returnCode': "SUCCESS", 'data':{}, 'errorCode':None}
    return jsonify(page)

@app.route('/deleteDoctor', methods=['POST'])
def deleteDoctor():
    doctorObj = request.get_json()
    deleteDoctorObj = Doctor.query.get(doctorObj['id'])
    db_session.delete(deleteDoctorObj)
    try:
        db_session.commit()
    except SQLAlchemyError as exception:
        db_session.rollback()
    page = {'returnCode': "SUCCESS", 'data':{}, 'errorCode':None}
    return jsonify(page)


@app.route('/addSpecialization', methods = ['POST'])
def addSpeciality():
    specJson = request.get_json()
    speciality = Speciality(specJson['name'])
    db_session.add(speciality)
    try:
        db_session.commit()
    except SQLAlchemyError as exception:
        db_session.rollback()
    page = {'returnCode': "SUCCESS", 'data':{}, 'errorCode':None}
    return jsonify(page)    


@app.route('/allSpecializations2')
def allSpecialities2():
    allSpecialities = Speciality.query.all()
    specList = []
    for spec in allSpecialities:
        specDetails={'specName':spec.name}
        specList.append(specDetails)
    return jsonify({'returnCode': "SUCCESS", 'data':specList, 'errorCode':None})

@app.route('/allSpecializations')
def allSpecialities():
    allSpecialities = Speciality.query.all()
    specList = []
    for spec in allSpecialities:
        specDetails={'name':spec.name}
        specList.append(specDetails)
    return jsonify({'returnCode': "SUCCESS", 'data':specList, 'errorCode':None})

@app.route('/allSpecializationsForDoctor')
def allSpecialitiesTwo():
    allSpecialities = Speciality.query.all()
    specList = []
    for spec in allSpecialities:
        specDetails = {'specName':spec.name}
        specList.append(specDetails)
    return jsonify({'returnCode': "SUCCESS", 'data':specList, 'errorCode':None})

@app.route('/deleteSpecialization', methods = ['POST'])
def deleteSpeciality():
      SpecObj = request.get_json()
      deleteSpeciality = Speciality.query.get(SpecObj['name'])
      db_session.delete(deleteSpeciality)
      try:
          db_session.commit()
      except SQLAlchemyError as exception:
          db_session.rollback()
      response = {'returnCode': "SUCCESS", 'data':{}, 'errorCode':None}
      return jsonify(response)

@app.route('/allDoctors', methods = ['GET'])
def allDoctors():
    allDoctors = Doctor.query.all()
    doctorList = []
    doctorDetails = {}
    for doc in allDoctors:
        clinicList = []
        specList = []
        ConsultList = []
        for clinic in doc.clinics:
            clinicList.append({
                'clinicId':clinic.id, 
                'clinicName':clinic.name,
                'clinicArea':clinic.area,
                'clinicContact':clinic.contact_no,
                'clinicAdddress':clinic.address
            })

        for speciality in doc.speciality:
            specList.append({'specName':speciality.name})

        doctorDetails = {
            'id' : doc.id, 
            'name': doc.name,
            'experience': doc.experience, 
            'email' : doc.email, 
            'education' : doc.education,
            'recommendations':doc.recommendations,
            'specList':specList,
            'clinicList':clinicList 
        }
        doctorList.append(doctorDetails)
    return jsonify({'returnCode': "SUCCESS", 'data':doctorList, 'errorCode':None})

@app.route('/allClinics', methods = ['GET'])
def allClinics():
    allClinics = Clinic.query.all()
    clinicList = []
    for clinic in allClinics:
        clinicDetails = {}
        clinicDetails = {
            'id':clinic.id, 
            'name':clinic.name,
            'area':clinic.area,
            'contact_no':clinic.contact_no,
            'city_name':clinic.city_name,
            'address':clinic.address
        }
        clinicList.append(clinicDetails)
    return jsonify({'returnCode': "SUCCESS", 'data':clinicList, 'errorCode':None})

@app.route('/addClinic', methods = ['POST'])
def addClinic():
    clinicJson = request.get_json()
    clinic = Clinic(
        clinicJson['name'], 
        clinicJson['area'], 
        clinicJson['address'],
        clinicJson['city_name'], 
        clinicJson['contact_no'])
    db_session.add(clinic)
    try:
        db_session.commit()
    except SQLAlchemyError as exception:
        db_session.rollback()
    page = {'returnCode': "SUCCESS", 'data':{}, 'errorCode':None}
    return jsonify(page)

@app.route('/editClinic', methods = ['POST'])
def editClinic():
    clinicObject = request.get_json()
    db_session.query(Clinic).filter_by(id = clinicObject['id']).update({
        'name': clinicObject['name'],
        'area': clinicObject['area'],
        'address': clinicObject['address'],
        'address': clinicObject['address'],
        'city_name': clinicObject['city_name']})
    try:
        db_session.commit()
    except SQLAlchemyError as exception:
        db_session.rollback()
    page = {'returnCode': "SUCCESS", 'data':{}, 'errorCode':None}
    return jsonify(page)

@app.route('/deleteClinic', methods = ['POST'])
def deleteClinic():
    clinicObj = request.get_json()
    deleteClinicObj = Clinic.query.get(clinicObj['id'])
    docClinicDelete = DoctorsClinic.query.filter(DoctorsClinic.clinicIdFk == clinicObj['id'])
    for assoc in docClinicDelete:
       db_session.delete(assoc)
       db_session.commit()
    db_session.delete(deleteClinicObj)
    try:
        db_session.commit()
    except SQLAlchemyError as exception:
        db_session.rollback()
    page = {'returnCode': "SUCCESS", 'data':{}, 'errorCode':None}
    return jsonify(page)

@app.route('/getClinic', methods = ['GET'])
def getClinic():
    allClinics = Clinic.query.all()
    clinicList = []
    for clinic in allClinics:
        clinicName = {}
        clinicName = {'name':clinic.name}
        clinicList.append(clinicName)
    print clinicList;
    return jsonify({'returnCode': "SUCCESS", 'data':clinicList, 'errorCode':None})

@app.route('/getClinicForDoctor', methods = ['GET'])
def getClinicTwo():
    allClinics = Clinic.query.all()
    clinicList = []
    for clinic in allClinics:
        clinicName = {}
        clinicName = {'clinicName':clinic.name}
        clinicList.append(clinicName)
    print clinicList;
    return jsonify({'returnCode': "SUCCESS", 'data':clinicList, 'errorCode':None})

@app.route('/getDoctorDetails/<int:did>', methods = ['GET'])
def getDoctor(did):
    doc = Doctor.query.get(did)
    clinicList = []
    specList = []
    ConsultList = []
    doctorList = []
    for clinic in doc.clinics:
        clinicList.append({
            'clinicId':clinic.id, 
            'clinicName':clinic.name,
            'clinicArea':clinic.area,
            'clinicContact':clinic.contact_no,
            'clinicAdddress':clinic.address
        })

    for speciality in doc.speciality:
        specList.append({'specName':speciality.name})

    doctorDetails = {
        'id' : doc.id, 
        'name': doc.name,
        'experience': doc.experience,
        'email' : doc.email, 
        'education' : doc.education,
        'recommendations': doc.recommendations,
        'specList': specList,
        'clinicList': clinicList }

    doctorList.append(doctorDetails)
    print doctorList
    return jsonify({'returnCode': "SUCCESS", 'data':doctorList, 'errorCode':None})

@app.route('/allLocalitySpeciality/', methods = ['GET'])
def AndroidFrontPage():
    allSpecialities = Speciality.query.all()
    allClinics = Clinic.query.all()
    specList = []
    clinicList = []
    SpecLocalityList = []
    for clinic in allClinics:
        clinicArea = {}
        clinicArea = {
            'area':clinic.area,
        }
        clinicList.append(clinicArea)
    for spec in allSpecialities:
        specDetails={'name':spec.name}
        specList.append(specDetails)

    Details = {'specList':specList,'clinicList':clinicList }
    SpecLocalityList.append(Details)
    return jsonify({'returnCode': "SUCCESS", 'data':SpecLocalityList, 'errorCode':None})
    










