from django.shortcuts import render
from rest_framework.response import Response
from rest_framework.viewsets import ViewSet
# Create your views here.



class helloViewset(ViewSet):
    def list(self, request):
        return Response({"MESSAGE": "Welcome to api "})
