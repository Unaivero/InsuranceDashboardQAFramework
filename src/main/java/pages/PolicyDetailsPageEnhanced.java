        public boolean isHasExpiryWarning() { return hasExpiryWarning; }
        public void setHasExpiryWarning(boolean hasExpiryWarning) { this.hasExpiryWarning = hasExpiryWarning; }
        
        @Override
        public String toString() {
            return String.format("PolicySummary{policyId='%s', status='%s', type='%s', insuredName='%s', documentsCount=%d, claimsCount=%d}", 
                    policyId, status, type, insuredName, documentsCount, claimsCount);
        }
    }
}
