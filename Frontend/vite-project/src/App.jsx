import React, { useState, useEffect } from 'react';
import axios from 'axios';
import 'bootstrap/dist/css/bootstrap.min.css';

function App() {
  const [word, setWord] = useState('');
  const [suggestions, setSuggestions] = useState([]);
  const [rankResult, setRankResult] = useState('');
  const [message, setMessage] = useState('');

  // Auto-complete with debounce
  useEffect(() => {
    const fetchSuggestions = async () => {
      if (word.length > 0) {
        try {
          const response = await axios.get(`http://localhost:8080/api/auto-complete?prefix=${word}`);
          setSuggestions(response.data);
        } catch (error) {
          setMessage('Error fetching suggestions');
        }
      } else {
        setSuggestions([]);
      }
    };

    const debounceTimer = setTimeout(fetchSuggestions, 300);
    return () => clearTimeout(debounceTimer);
  }, [word]);

  const handleSearch = async (e) => {
    e.preventDefault();
    try {
      await axios.post(`http://localhost:8080/api/search?word=${word}`);
      setMessage('Search recorded successfully');
      setSuggestions([]);
    } catch (error) {
      setMessage('Error recording search');
    }
  };

  const handleRank = async () => {
    try {
      const response = await axios.get(`http://localhost:8080/api/rank?word=${word}`);
      setRankResult(response.data.rank !== undefined ? 
        `Rank of "${word}": ${response.data.rank}` : 
        'Word not found');
    } catch (error) {
      setRankResult('Error fetching rank');
    }
  };

  const handleInsert = async (e) => {
    e.preventDefault();
    try {
      await axios.post(`http://localhost:8080/api/insert?word=${word}`);
      setMessage(`"${word}" inserted successfully`);
      setSuggestions([]);
    } catch (error) {
      setMessage('Error inserting word');
    }
  };

  return (
    <div className="container mt-5">
      <h1 className="mb-4">Word Search System</h1>
      
      <div className="row mb-4">
        <div className="col-md-8">
          <form onSubmit={handleSearch}>
            <div className="input-group">
              <input
                type="text"
                className="form-control"
                value={word}
                onChange={(e) => setWord(e.target.value)}
                placeholder="Enter a word..."
              />
              <button className="btn btn-primary" type="submit">
                Search
              </button>
              <button className="btn btn-info" type="button" onClick={handleRank}>
                Get Rank
              </button>
              <button className="btn btn-success" type="button" onClick={handleInsert}>
                Insert
              </button>
            </div>
          </form>

          {suggestions.length > 0 && (
            <div className="list-group mt-2">
              {suggestions.map((suggestion, index) => (
                <button
                  key={index}
                  type="button"
                  className="list-group-item list-group-item-action"
                  onClick={() => {
                    setWord(suggestion);
                    handleSearch({ preventDefault: () => {} });
                  }}
                >
                  {suggestion}
                </button>
              ))}
            </div>
          )}
        </div>
      </div>

      {message && (
        <div className="alert alert-info" role="alert">
          {message}
        </div>
      )}

      {rankResult && (
        <div className="alert alert-warning" role="alert">
          {rankResult}
        </div>
      )}
    </div>
  );
}

export default App;