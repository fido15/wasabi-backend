from rest_framework import serializers
from .models import NewSite, RssData, Category

class NewSiteSerializer(serializers.ModelSerializer):
    class Meta:
        model = NewSite
        fields = ['id', 'link', 'reliability']

class RssDataSerializer(serializers.ModelSerializer):
    class Meta:
        model = RssData
        fields = ['id', 'title', 'link', 'published', 'updated', 'summary', 'country']

class CategorySerializer(serializers.ModelSerializer):
    class Meta:
        model = Category
        fields = ['id', 'name']
