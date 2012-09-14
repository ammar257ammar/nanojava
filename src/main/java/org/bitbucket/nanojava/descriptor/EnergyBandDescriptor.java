/* Copyright (C) 2011  Egon Willighagen <egonw@users.sf.net>
 *
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 */
package org.bitbucket.nanojava.descriptor;

import java.util.ArrayList;
import java.util.List;

import org.bitbucket.nanojava.data.Nanomaterial;
import org.bitbucket.nanojava.data.measurement.IMeasurement;
import org.bitbucket.nanojava.data.measurement.MeasurementRange;
import org.bitbucket.nanojava.data.measurement.MeasurementValue;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IIsotope;
import org.openscience.cdk.interfaces.IMolecularFormula;
import org.openscience.cdk.qsar.DescriptorSpecification;
import org.openscience.cdk.qsar.DescriptorValue;
import org.openscience.cdk.qsar.result.DoubleArrayResult;
import org.openscience.cdk.qsar.result.DoubleArrayResultType;
import org.openscience.cdk.qsar.result.IDescriptorResult;

public class EnergyBandDescriptor implements INanomaterialDescriptor {

	public String[] getDescriptorNames() {
        return new String[]{"CBandE", "VBandE"};
	}

	public String[] getParameterNames() {
        return new String[0];
	}

	public Object getParameterType(String arg0) {
		return null;
	}

	public Object[] getParameters() {
		return new Object[0];
	}

	public DescriptorSpecification getSpecification() {
	    return new DescriptorSpecification(
	        "http://egonw.github.com/resource/NM_001001",
	        this.getClass().getName(),
	        "$Id: 9927243df29a118e9bfd0b8624bc8d77d3c6db07 $",
	        "The NanoJava Project"
	    );
	}

	public void setParameters(Object[] arg0) throws CDKException {
		return; // no parameters
	}

	public DescriptorValue calculate(Nanomaterial container) {
        if (container == null) return newNaNDescriptor();

	    IMolecularFormula molFormula = container.getChemicalComposition();
	    if (molFormula == null) return newNaNDescriptor();
	    
	    int elementCount = getElementCount(molFormula);
	    if (elementCount != 2) return newNaNDescriptor();
	    
	    IMeasurement size = container.getSize();
	    if (size != null) {
	        // check the size
	        if (size instanceof MeasurementRange) {
	            MeasurementRange range = (MeasurementRange)size;
	            if (range.getMaximumValue() < 30) { // too small
	                // TODO: I should check units at some point
	                return newNaNDescriptor();
	            }
	        } else if (size instanceof MeasurementValue) {
	            MeasurementValue value = (MeasurementValue)size;
	            if (value.getValue() < 20) { // too small
	                // TODO: I should check units at some point
                    return newNaNDescriptor();
	            }
	        }
	    }

        DoubleArrayResult result = new DoubleArrayResult(2);
	    for (IIsotope isotope : molFormula.isotopes()) {
	        int oCount = getElementCount(molFormula, "O");
	        if ("Zn".equals(isotope.getSymbol())) {
	            if (oCount == 1) {
	                result.add(-3.7);
	                result.add(-7.25);
	            } else continue;
	        } else if ("Cu".equals(isotope.getSymbol())) {
	            if (oCount == 1) {
	                int cuCount = getElementCount(molFormula, "Cu");
	                if (cuCount == 1) {
	                    result.add(-4.7);
	                    result.add(-6.7);
	                } else if (cuCount == 2) {
	                    result.add(-4.4);
	                    result.add(-5.6);
	                } else continue;
	            } else continue;
            } else if ("Ti".equals(isotope.getSymbol())) {
                if (oCount == 2) {
                    result.add(-4.2);
                    result.add(-7.5);
                } else if (oCount == 1) {
                    result.add(-1.8);
                    result.add(-8);
                } else continue;
            } else if ("Fe".equals(isotope.getSymbol())) {
                if (oCount == 1) {
                    result.add(-4.25);
                    result.add(-6.8);
                } else if (oCount == 3) {
                    result.add(-4.25);
                    result.add(-7.6);
                } else if (oCount == 4) {
                    result.add(-5.7);
                    result.add(-5.8);
                } else continue;
	        } else {
	            continue;
	        }

	        return new DescriptorValue(
	                getSpecification(),
	                getParameterNames(),
	                getParameters(),
	                result,
	                getDescriptorNames()
	        );
	    }
	    
	    return newNaNDescriptor();
	}

	private int getElementCount(IMolecularFormula molFormula) {
	    int count = 0;
	    List<String> alreadyElements = new ArrayList<String>();
        for (IIsotope isotope : molFormula.isotopes()) {
            if (!alreadyElements.contains(isotope.getSymbol())) {
                count++;
                alreadyElements.add(isotope.getSymbol());
            }
        }
        return count;
    }

    private int getElementCount(IMolecularFormula molFormula, String element) {
        int count = 0;
        for (IIsotope isotope : molFormula.isotopes()) {
            if (element.equals(isotope.getSymbol())) {
                count += molFormula.getIsotopeCount(isotope);
            }
        }
        return count;
    }

    private DescriptorValue newNaNDescriptor() {
        DoubleArrayResult result = new DoubleArrayResult(2);
        result.add(Double.NaN);
        result.add(Double.NaN);
	    return new DescriptorValue(
	        getSpecification(),
	        getParameterNames(),
	        getParameters(),
	        result,
	        getDescriptorNames()
	    );
    }

    public IDescriptorResult getDescriptorResultType() {
		return new DoubleArrayResultType(2);
	}

}