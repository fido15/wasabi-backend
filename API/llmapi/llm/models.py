from django.db import models

# Create your models here.
from django.db import models

class NewSite(models.Model):
    link = models.URLField(max_length=200)  
    reliability = models.FloatField()  

    def __str__(self):
        return self.link

class RssData(models.Model):
    title = models.TextField()  
    link = models.URLField(max_length=200)  
    published = models.DateTimeField() 
    updated = models.DateTimeField(null=True, blank=True)  
    summary = models.TextField()  
    country = models.CharField(max_length=100)  

    def __str__(self):
        return self.title

class Category(models.Model):
    name = models.CharField(max_length=100)  

    def __str__(self):
        return self.name
