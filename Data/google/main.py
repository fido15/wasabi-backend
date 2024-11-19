from flask import Flask, request, jsonify
from googlesearch import search

app = Flask(__name__)

@app.route('/search', methods=['GET'])
def google_search():
    query = request.args.get('query')
    if not query:
        return jsonify({'error': 'No query provided'}), 400

    try:
        search_results = search(query, num_results=50)
        return jsonify({'query': query, 'results': list(search_results)})
    except Exception as e:
        return jsonify({'error': str(e)}), 500

if __name__ == '__main__':
    app.run(debug=True)
