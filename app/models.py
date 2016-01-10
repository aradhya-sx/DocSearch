from sqlalchemy import Column, Integer, String, ForeignKey
from database import Base
from sqlalchemy.orm import relationship, backref 
from sqlalchemy.exc import SQLAlchemyError


class Speciality(Base):
    __tablename__= 'speciality'
    name = Column(String(100), primary_key = True)
    doctors = relationship('Doctor', secondary = 'doctorsspeciality');
    def __init__(self, name):
        self.name=name  

class Doctor(Base):
    __tablename__ = 'doctor'
    id = Column(Integer, primary_key = True, autoincrement = True, index = True)
    name = Column(String(100), nullable = False)
    email = Column(String(100), nullable = False, unique = True)
    experience = Column(Integer, nullable = False)
    education = Column(String(100), nullable = False)
    recommendations = Column(Integer, nullable = False)
    clinics = relationship('Clinic', secondary = 'doctorsclinic');
    speciality = relationship('Speciality', secondary = "doctorsspeciality");
    def __init__(self, name, email, experience, education, recommendations):
        self.name = name
        self.email = email
        self.experience = experience
        self.education = education
        self.recommendations = recommendations
    def __repr__(self):
        return '<Doctor %r>' % (self.name)

class DoctorsSpeciality(Base):
    __tablename__ = 'doctorsspeciality'
    doctorIdFK = Column(Integer, ForeignKey('doctor.id'), primary_key = True); 
    specialityFK = Column(String(100), ForeignKey('speciality.name'), primary_key = True, index = True);
    doctorInfo = relationship('Doctor', backref = backref('doctorsRelated',\
                                        cascade = 'save-update, merge, delete, all, delete-orphan'))
    specialityInfo = relationship('Speciality', backref = backref('specialitiesRelated',\
                                                cascade = 'save-update, merge, delete, all, delete-orphan'))

class Clinic(Base):
    __tablename__ = 'clinic'
    id = Column(Integer, primary_key = True, autoincrement = True, index = True)
    name = Column(String(50), nullable = False)
    area = Column(String(120), nullable = False, index = True)
    address = Column(String(300), nullable = False)
    city_name = Column(String(120), nullable = False)
    contact_no = Column(String(120), nullable = False)
    doctors = relationship('Clinic', secondary = 'doctorsclinic');
    def __init__(self, name, area, address, city_name, contact_no):
        self.name = name
        self.area = area
        self.address = address
        self.city_name = city_name
        self.contact_no = contact_no
    def __repr__(self):
        return '<Clinic %r>' % (self.name)

class DoctorsClinic(Base):
    __tablename__ = 'doctorsclinic'
    doctorIdFK = Column(Integer,ForeignKey('doctor.id'), primary_key = True, index = True); 
    clinicIdFk = Column(Integer,ForeignKey('clinic.id'), primary_key = True, index = True);
    fees = Column(Integer)
    timings = Column(String(120))
    relatedDoctors = relationship('Doctor', backref = backref('doctorsClinicAssoc',\
                                            cascade = 'save-update,merge,delete,all,delete-orphan'));
    relatedClinics = relationship('Clinic', backref = backref('clinicDoctorAssoc',\
                                            cascade = 'save-update, merge, delete, all, delete-orphan'));
    def __init__(self,doctorIdFK,clinicIdFk): 
        self.doctorIdFK = doctorIdFK
        self.clinicIdFk = clinicIdFk




