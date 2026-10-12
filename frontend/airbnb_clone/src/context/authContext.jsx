import { createContext, useState, useContext } from 'react';
import { logout } from '../services/apiRequests';

const AuthContext = createContext();

export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(null);
  
    const logoutUser = () => {
      logout()
      setUser(null);
    };

    const isAdmin = user && user.role === 'ADMIN';
    const isOwner = user && user.role === 'OWNER';

  return (
    <AuthContext.Provider value={{ user, setUser, logoutUser, isAdmin, isOwner }}>
      {children}
    </AuthContext.Provider>
  );
};

// eslint-disable-next-line react-refresh/only-export-components -- comportamiento existente, ver Backlog
export const useAuth = () => {
  return useContext(AuthContext);
};
