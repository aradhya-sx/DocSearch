import random
from elasticsearch import Elasticsearch
from elasticsearch_dsl import Search

es = Elasticsearch()
s = Search(es)
dictmatch={ "doctors.location" : "Jayanagar" }
hits=s.query("match", **dictmatch).extra(from_=5,size=1).execute()

hits=hits.to_dict()
for hit in hits['hits']['hits']:
	print str(hit['_source']['doctors'])