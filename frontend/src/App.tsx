import React, { FormEvent, useCallback, useEffect, useMemo, useState } from 'react';

type Colleague = {
  id: number;
  name: string;
  cups: number;
};

const DEFAULT_API_BASE = 'http://localhost:8080/api';

const App: React.FC = () => {
  const apiBase = useMemo(
    () => (import.meta.env.VITE_API_BASE_URL as string | undefined) ?? DEFAULT_API_BASE,
    []
  );

  const [toplist, setToplist] = useState<Colleague[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [nameInput, setNameInput] = useState('');
  const [isSubmitting, setIsSubmitting] = useState(false);

  const fetchToplist = useCallback(async () => {
    setLoading(true);
    setError(null);
    try {
      const response = await fetch(`${apiBase}/colleagues/toplist`);
      if (!response.ok) {
        throw new Error(`Failed to load toplist (${response.status})`);
      }
      const data: Colleague[] = await response.json();
      setToplist(data);
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Unknown error while loading toplist');
    } finally {
      setLoading(false);
    }
  }, [apiBase]);

  useEffect(() => {
    void fetchToplist();
  }, [fetchToplist]);

  const handleAddColleague = async (event: FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    const trimmed = nameInput.trim();
    if (!trimmed) {
      setError('Please enter a name before adding a colleague.');
      return;
    }

    setIsSubmitting(true);
    setError(null);
    try {
      const response = await fetch(`${apiBase}/colleagues`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ name: trimmed }),
      });
      if (!response.ok) {
        const { error } = await response.json().catch(() => ({ error: 'Failed to add colleague' }));
        throw new Error(typeof error === 'string' ? error : 'Failed to add colleague');
      }
      setNameInput('');
      await fetchToplist();
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Unknown error while adding colleague');
    } finally {
      setIsSubmitting(false);
    }
  };

  const handleAddCup = async (colleague: Colleague) => {
    setError(null);
    try {
      const response = await fetch(`${apiBase}/colleagues/${colleague.id}/cups`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ cups: 1 }),
      });
      if (!response.ok) {
        throw new Error(`Failed to add cup for ${colleague.name}`);
      }
      await fetchToplist();
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Unknown error while adding cup');
    }
  };

  const hasColleagues = toplist.length > 0;

  return (
    <main className="app">
      <header>
        <h1>Office Coffee Toplist</h1>
        <p>Track how many cups of coffee your teammates have enjoyed today.</p>
      </header>

      <section>
        <h2>Add a colleague</h2>
        <form onSubmit={handleAddColleague}>
          <label htmlFor="name-input">Name</label>
          <input
            id="name-input"
            name="name"
            type="text"
            value={nameInput}
            onChange={(event) => setNameInput(event.target.value)}
            placeholder="e.g. Alex"
            disabled={isSubmitting}
            required
          />
          <button type="submit" disabled={isSubmitting}>
            {isSubmitting ? 'Adding…' : 'Add colleague'}
          </button>
        </form>
      </section>

      <section>
        <h2>Top coffee drinkers</h2>

        {loading && <p>Loading toplist…</p>}
        {!loading && !hasColleagues && <p>No colleagues yet. Add someone to get started!</p>}

        {!loading && hasColleagues && (
          <ol>
            {toplist.map((colleague) => (
              <li key={colleague.id}>
                <span>
                  <strong>{colleague.name}</strong> — {colleague.cups} cups
                </span>
                <button type="button" onClick={() => void handleAddCup(colleague)}>
                  +1 cup
                </button>
              </li>
            ))}
          </ol>
        )}
      </section>

      {error && (
        <section aria-live="assertive">
          <p role="alert">{error}</p>
        </section>
      )}
    </main>
  );
};

export default App;
