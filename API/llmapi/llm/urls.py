from django.urls import include, path
#Landry
from rest_framework import routers

from .views import *

router = routers.DefaultRouter()
router.register(r'Welcome',helloViewset, basename='welcome')


# Wire up our API using automatic URL routing.   
urlpatterns = [
    path('', include(router.urls)),
]