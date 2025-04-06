import React, { useState, useEffect } from 'react';
import axios from 'axios';
import 'bootstrap/dist/css/bootstrap.min.css';
import { FiSearch, FiTrendingUp, FiPlusCircle, FiInfo } from 'react-icons/fi';

function App() {
  const [word, setWord] = useState('');
  const [suggestions, setSuggestions] = useState([]);
  const [rankResult, setRankResult] = useState('');
  const [message, setMessage] = useState('');
  const [isLoading, setIsLoading] = useState(false);

  // Auto-complete with debounce
  useEffect(() => {
    const fetchSuggestions = async () => {
      if (word.length > 0) {
        try {
          const response = await axios.get(`http://localhost:8080/api/auto-complete?prefix=${word}`);
          setSuggestions(response.data);
        } catch (error) {
          showTimedMessage('Error fetching suggestions', 'error');
        }
      } else {
        setSuggestions([]);
      }
    };

    const debounceTimer = setTimeout(fetchSuggestions, 300);
    return () => clearTimeout(debounceTimer);
  }, [word]);

  const showTimedMessage = (text, type = 'info') => {
    setMessage({ text, type });
    setTimeout(() => setMessage({ text: '', type: '' }), 3000);
  };

  const handleSearch = async (e) => {
    e.preventDefault();
    setIsLoading(true);
    setMessage({ text: '', type: '' });
    setRankResult('');

    try {
      const response = await axios.post(`http://localhost:8080/api/search?word=${word}`);
      showTimedMessage(response.data.message, 'success');
      setSuggestions([]);
    } catch (error) {
      showTimedMessage(error.response?.data?.message || 'Error recording search', 'error');
    } finally {
      setIsLoading(false);
    }
  };

  const handleRank = async () => {
    setIsLoading(true);
    setMessage({ text: '', type: '' });

    try {
      const response = await axios.get(`http://localhost:8080/api/rank?word=${word}`);
      setRankResult({
        // text: `"${word}" is ranked #${response.data.rank}`,
        text: `"Rank of ${word} is ${response.data.rank}`,
        type: 'success'
      });
    } catch (error) {
      setRankResult({
        text: error.response?.status === 404 ? 'Word not found' : 'Error fetching rank',
        type: 'error'
      });
    } finally {
      setIsLoading(false);
    }
  };

  const handleInsert = async (e) => {
    e.preventDefault();
    setIsLoading(true);
    setMessage({ text: '', type: '' });
    setRankResult('');

    try {
      const response = await axios.post(`http://localhost:8080/api/insert?word=${word}`);
      showTimedMessage(response.data, 'success');
      setSuggestions([]);
    } catch (error) {
      showTimedMessage(error.response?.data?.message || 'Error inserting word', 'error');
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <div className="container mt-5">
      <div className="card shadow-lg">
        <div className="card-header bg-primary text-white">
          <h2 className="mb-0"><FiSearch className="me-2" />Word Search System</h2>
        </div>
        
        <div className="card-body">
          <div className="row mb-4">
            <div className="col-md-8 mx-auto">
              <form onSubmit={handleSearch}>
                <div className="input-group">
                  <input
                    type="text"
                    className="form-control form-control-lg border-primary"
                    value={word}
                    onChange={(e) => setWord(e.target.value)}
                    placeholder="Enter a word..."
                    disabled={isLoading}
                  />
                  
                  <div className="input-group-append">
                    <button 
                      className="btn btn-primary" 
                      type="submit"
                      disabled={isLoading}
                    >
                      <FiSearch className="me-2" />
                      {isLoading ? 'Searching...' : 'Search'}
                    </button>
                    
                    <button 
                      className="btn btn-info text-white" 
                      type="button" 
                      onClick={handleRank}
                      disabled={isLoading}
                    >
                      <FiTrendingUp className="me-2" />
                      Rank
                    </button>
                    
                    <button 
                      className="btn btn-success" 
                      type="button" 
                      onClick={handleInsert}
                      disabled={isLoading}
                    >
                      <FiPlusCircle className="me-2" />
                      Insert
                    </button>
                  </div>
                </div>

                {suggestions.length > 0 && (
                  <div className="list-group mt-3 shadow-sm">
                    {suggestions.map((suggestion, index) => (
                      <button
                        key={index}
                        type="button"
                        className="list-group-item list-group-item-action d-flex justify-content-between align-items-center"
                        onClick={() => {
                          setWord(suggestion);
                          handleSearch({ preventDefault: () => {} });
                        }}
                      >
                        {suggestion}
                        <FiInfo className="text-muted" />
                      </button>
                    ))}
                  </div>
                )}
              </form>
            </div>
          </div>

          <div className="row">
            <div className="col-md-8 mx-auto">
              {message.text && (
                <div className={`alert alert-${message.type === 'error' ? 'danger' : 'success'} fade show`} role="alert">
                  {message.text}
                </div>
              )}

              {rankResult.text && (
                <div className={`alert alert-${rankResult.type === 'error' ? 'danger' : 'warning'} fade show`} role="alert">
                  {rankResult.text}
                </div>
              )}
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}

export default App;