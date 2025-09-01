import React from "react";
import "./PrivacyPolicy.css";

const PrivacyPolicy: React.FC = () => {
  return (
    <div className="privacy-policy">
      <h1>Privacy Policy</h1>
      <p>
        In compliance with <strong>Regulation (EU) 2016/679 (GDPR)</strong>,
        concerning the protection of natural persons with regard to the
        processing of personal data and the free movement of such data, and{" "}
        <strong>Spanish Organic Law 3/2018 (LOPDGDD)</strong>, users of this
        website are informed of the following:
      </p>

      <h2>1. Data Controller</h2>
      <p>
        <strong>Federico López Pérez</strong>
        <br />
        <strong>Tax ID:</strong> 32091029A
        <br />
        <strong>Email:</strong> fedevlopez17@gmail.com
        <br />
        If a Data Protection Officer (DPO) is appointed, their contact details
        will be provided here.
      </p>

      <h2>2. Personal Data We Process</h2>
      <ul>
        <li>Contact form: name, surname, and email address.</li>
        <li>Newsletter subscription: email address.</li>
      </ul>

      <h2>3. Purpose of Processing</h2>
      <p>The personal data is used for:</p>
      <ul>
        <li>
          Responding to inquiries and requests sent through the contact form.
        </li>
        <li>
          Managing the sending of newsletters and communications related to our
          services, provided the user has voluntarily subscribed.
        </li>
      </ul>

      <h2>4. Legal Basis for Processing</h2>
      <p>
        The processing of personal data is based on the{" "}
        <strong>explicit consent</strong> of the user, given by submitting the
        corresponding forms or subscribing to the newsletter.
      </p>

      <h2>5. Data Retention Period</h2>
      <p>
        The data will be retained for as long as there is a mutual interest in
        maintaining the relationship and as long as the user does not request
        its deletion. For the newsletter, the data will be deleted immediately
        when the user unsubscribes.
      </p>

      <h2>6. Data Recipients</h2>
      <p>
        Data will not be disclosed to third parties except when legally
        required. For service provision, we use third-party providers such as{" "}
        <strong>Google (Gmail Email Service)</strong> for sending emails, which
        may involve international data transfers to the United States,
        safeguarded by the <strong>EU-U.S. Data Privacy Framework</strong>.
        Additionally, the hosting provider may have access to the data for
        service maintenance purposes.
      </p>

      <h2>7. User Rights</h2>
      <p>The user has the right to:</p>
      <ul>
        <li>Access their personal data.</li>
        <li>Request rectification or deletion of their data.</li>
        <li>Request restriction of processing.</li>
        <li>Object to the processing.</li>
        <li>Request data portability.</li>
      </ul>
      <p>
        To exercise these rights, the user must send an email to{" "}
        <strong>fedevlopez17@gmail.com</strong>, specifying the right they wish to
        exercise and attaching a copy of a valid identification document.  
        In case of disagreement, the user can file a complaint with the{" "}
        <a
          href="https://www.aepd.es"
          target="_blank"
          rel="noopener noreferrer"
        >
          Spanish Data Protection Agency
        </a>.
      </p>

      <h2>8. Data Security</h2>
      <p>
        We have implemented the necessary technical and organizational measures
        to ensure the security and integrity of the processed personal data, as
        well as to prevent its loss, alteration, and unauthorized access.
      </p>

      <h2>9. Changes to This Policy</h2>
      <p>
        We reserve the right to modify this Privacy Policy to adapt it to legal
        or jurisprudential developments. Any modification will be published on
        this same page.
      </p>

      <p>
        <strong>Last updated:</strong> 15/08/2025
      </p>
    </div>
  );
};

export default PrivacyPolicy;
